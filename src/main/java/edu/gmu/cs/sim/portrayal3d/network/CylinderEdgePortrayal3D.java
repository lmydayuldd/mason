/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package edu.gmu.cs.sim.portrayal3d.network;

import javax.media.j3d.Appearance;
import javax.media.j3d.Node;
import java.awt.*;

import com.sun.j3d.utils.geometry.Cylinder;
import edu.gmu.cs.sim.portrayal3d.simple.PrimitivePortrayal3D;

/**
 * @author Gabriel Balan
 *
 */
public class CylinderEdgePortrayal3D extends PrimitiveEdgePortrayal3D {
    /** @deprecated. */
    public CylinderEdgePortrayal3D(double radius) {
        this(null, Color.white, null, radius);
    }

    /** @deprecated */
    public CylinderEdgePortrayal3D(Color labelColor) {
        this(null, labelColor, null, DEFAULT_RADIUS);
    }

    /** @deprecated */
    public CylinderEdgePortrayal3D(double radius, Color labelColor) {
        this(null, labelColor, null, radius);
    }

    public CylinderEdgePortrayal3D() {
        this(null, Color.white, null, DEFAULT_RADIUS);
    }

    public CylinderEdgePortrayal3D(Appearance appearance, Color labelColor) {
        this(appearance, labelColor, null, DEFAULT_RADIUS);
    }

    public CylinderEdgePortrayal3D(Color color, Color labelColor) {
        this(appearanceForColor(color), labelColor, null, DEFAULT_RADIUS);
    }

    /** Assumes that the image is opaque */
    public CylinderEdgePortrayal3D(Image image, Color labelColor) {
        this(appearanceForImage(image, true), labelColor, null, DEFAULT_RADIUS);
    }

    public CylinderEdgePortrayal3D(Appearance appearance, Color labelColor, Font labelFont, double radius) {
        super(new Cylinder((float) radius, (float) DEFAULT_HEIGHT), appearance, labelColor, labelFont);
    }


    protected void init(Node edgeModel) {
        super.init(edgeModel);
        Cylinder c = (Cylinder) edgeModel;
        PrimitivePortrayal3D.setShape3DFlags(c.getShape(Cylinder.BODY));
        PrimitivePortrayal3D.setShape3DFlags(c.getShape(Cylinder.TOP));
        PrimitivePortrayal3D.setShape3DFlags(c.getShape(Cylinder.BOTTOM));
    }

    // top, bottom, body.
    protected int numShapes() {
        return 3;
    }
}
