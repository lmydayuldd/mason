/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package edu.gmu.cs.sim.portrayal.inspector;

import java.awt.*;

import edu.gmu.cs.sim.display.ChartUtilities;
import edu.gmu.cs.sim.display.GUIState;
import edu.gmu.cs.sim.engine.Stoppable;
import edu.gmu.cs.sim.util.DoubleBag;
import edu.gmu.cs.sim.util.IntBag;
import edu.gmu.cs.sim.util.Properties;
import edu.gmu.cs.sim.util.Valuable;
import edu.gmu.cs.sim.util.media.chart.ChartGenerator;
import edu.gmu.cs.sim.util.media.chart.HistogramGenerator;
import edu.gmu.cs.sim.util.media.chart.XYChartGenerator;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.general.SeriesChangeListener;

/** A property inspector which generates histograms of data.  The histograms update in real-time as
 requested by the user.  Data properties for which
 the HistogramChartingPropertyInspector will operate include:

 <ul>
 <li>Any array of numerical values (byte[], int[], double[], etc.)
 <li>Any array of sim.util.Valuable
 <li>Any array of Numbers (Double[], Integer[], etc.)
 <li>Any sim.util.IntBag
 <li>Any sim.util.DoubleBag
 </ul>

 <p>HistogramChartingPropertyInspector registers itself with the property menu option "Make Histogram".
 */

public class HistogramChartingPropertyInspector extends ChartingPropertyInspector {
    /** The initial number of bins in histograms. */
    public static final int DEFAULT_BINS = 8;
    double[] previousValues = new double[]{0};  // sacrificial

    protected boolean validChartGenerator(ChartGenerator generator) {
        return generator instanceof HistogramGenerator;
    }

    protected boolean includeAggregationMethodAttributes() {
        return false;
    }

    public static String name() {
        return "Make Histogram";
    }

    public static Class[] types() {
        return new Class[]
            {
                new byte[0].getClass(), new short[0].getClass(), new int[0].getClass(), new long[0].getClass(),
                new float[0].getClass(), new double[0].getClass(), new boolean[0].getClass(), new Valuable[0].getClass(),
                new Number[0].getClass(), IntBag.class, DoubleBag.class,
                ChartUtilities.ProvidesDoubles.class
            };
    }

    public HistogramChartingPropertyInspector(Properties properties, int index, Frame parent, final GUIState simulation) {
        super(properties, index, parent, simulation);
        setupSeriesAttributes(properties, index);
    }

    public HistogramChartingPropertyInspector(Properties properties, int index, final GUIState simulation, ChartGenerator generator) {
        super(properties, index, simulation, generator);
        setupSeriesAttributes(properties, index);
    }

    //I isolated this code from the constructor into this method because I have two constructors now. 
    private void setupSeriesAttributes(Properties properties, int index) {
        if (isValidInspector()) {
            if (getGenerator().getNumSeriesAttributes() == 0)  // recall that we've not been added yet
            {
                // take control
                getGenerator().setTitle("" + properties.getName(index) + " of " + properties.getObject());
                ((XYChartGenerator) getGenerator()).setYAxisLabel("Frequency");
                ((XYChartGenerator) getGenerator()).setXAxisLabel(properties.getName(index));
            }

            // add our series
            seriesAttributes = ((HistogramGenerator) generator).addSeries(previousValues, DEFAULT_BINS, properties.getName(index),
                new SeriesChangeListener() {
                    public void seriesChanged(SeriesChangeEvent event) {
                        getStopper().stop();
                    }
                });
        }
    }

    protected ChartGenerator createNewGenerator() {
        return new HistogramGenerator() {
            public void quit() {
                super.quit();
                Stoppable stopper = getStopper();
                if (stopper != null) {
                    stopper.stop();
                }

                // remove the chart from the GUIState's charts
                getCharts(simulation).remove(this);
            }
        };
    }

    public void updateSeries(double time, double lastTime) {
        Object obj = properties.getValue(index);
        if (obj == null) {
            return;
        }
        Class cls = obj.getClass();
        double[] vals = previousValues;  // set it to something in case we don't get anything new.

        if (cls.isArray()) {
            Class comp = cls.getComponentType();
            // this is gonna be long
            if (comp.equals(Byte.TYPE)) {
                byte[] array = (byte[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i];
                }
            } else if (comp.equals(Short.TYPE)) {
                short[] array = (short[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i];
                }
            } else if (comp.equals(Integer.TYPE)) {
                int[] array = (int[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i];
                }
            } else if (comp.equals(Long.TYPE)) {
                long[] array = (long[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i];
                }
            } else if (comp.equals(Float.TYPE)) {
                float[] array = (float[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i];
                }
            } else if (comp.equals(Double.TYPE)) {
                // yeah, yeah, yeah, double->double...
                double[] array = (double[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i];
                }
            } else if (comp.equals(Boolean.TYPE)) {
                boolean[] array = (boolean[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = (array[i] ? 1 : 0);
                }
            } else if (comp.equals(Valuable.class)) {
                Valuable[] array = (Valuable[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i].doubleValue();
                }
            } else if (comp.equals(Number.class)) {
                Number[] array = (Number[]) obj;
                vals = new double[array.length];
                for (int i = 0; i < array.length; i++) {
                    vals[i] = array[i].doubleValue();
                }
            }
        } else if (obj instanceof IntBag) {
            IntBag bag = (IntBag) (obj);
            vals = new double[bag.numObjs];
            for (int i = 0; i < bag.numObjs; i++) {
                vals[i] = (bag.objs[i]);
            }
        } else if (obj instanceof DoubleBag) {
            DoubleBag bag = (DoubleBag) (obj);
            vals = new double[bag.numObjs];
            for (int i = 0; i < bag.numObjs; i++) {
                vals[i] = (bag.objs[i]);
            }
        } else if (obj instanceof ChartUtilities.ProvidesDoubles) {
            double[] array = ((ChartUtilities.ProvidesDoubles) obj).provide();
            vals = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                vals[i] = array[i];
            }
        }

        boolean same = true;
        if (previousValues != null && vals.length == previousValues.length) {
            for (int i = 0; i < vals.length; i++) {
                if (vals[i] != previousValues[i]) {
                    same = false;
                    break;
                }
            }
        } else {
            same = false;
        }

        if (same) {
            return;  // they're identical
        }

        // at this point we're committed to do an update
        previousValues = vals;

        ((HistogramGenerator) generator).updateSeries(seriesAttributes.getSeriesIndex(), vals);
    }
}
