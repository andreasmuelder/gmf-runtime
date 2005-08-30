/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts;

import org.eclipse.draw2d.IFigure;

import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeEllipseFigure;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.draw2d.figures.GeoShapeFigure;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author jschofie
 *
 * Controls the interactions between the figure and
 * its underlying view 
 */
public class EllipseEditPart extends GeoShapeEditPart {
	
	private static final int FIGURE_WIDTH = MapMode.DPtoLP(50);
	private static final int FIGURE_HEIGHT = FIGURE_WIDTH;

	/**
	 * Constructor - Create an EditPart for a given model object (View)
	 * 
	 * @param shapeView model object that represents the associated figure
	 */
	public EllipseEditPart(View shapeView) {
	
		super(shapeView);
	}
	
	/**
	 * @see com.rational.xtools.presentation.editparts.ShapeNodeEditPart#createNodeFigure()
	 */
	protected NodeFigure createNodeFigure() {
		return new GeoShapeEllipseFigure( FIGURE_WIDTH, FIGURE_HEIGHT );
	}

	/**
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane() {
		return ((GeoShapeFigure) getFigure()).getContentPane();
	}
}
