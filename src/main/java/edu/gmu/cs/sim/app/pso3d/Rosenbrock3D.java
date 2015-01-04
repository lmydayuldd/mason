/*
  Copyright 2006 by Ankur Desai, Sean Luke, and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package edu.gmu.cs.sim.app.pso3d;

/**
 @author Ankur Desai and Joey Harrison
 */
public class Rosenbrock3D implements Evaluatable3D {
    private static final long serialVersionUID = 1;

    public double calcFitness(double x, double y, double z) {
        return (1000 - (100 *
            ((x * x - y) * (x * x - y) + (1 - x) * (1 - x)) +
            ((y * y - z) * (y * y - z) + (1 - y) * (1 - y))
        ));
    }
}
