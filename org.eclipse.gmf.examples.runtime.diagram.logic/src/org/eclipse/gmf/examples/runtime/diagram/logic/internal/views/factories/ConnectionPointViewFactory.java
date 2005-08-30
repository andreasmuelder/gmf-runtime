/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.figures.LogicColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.ShapeStyle;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author mmostafa
 */
public class ConnectionPointViewFactory extends AbstractShapeViewFactory {
	/**
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	public View createView(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint,
		int index,
		boolean persisted, final PreferencesHint preferencesHint) {
		View view =  super.createView(
			semanticAdapter,
			containerView,
			semanticHint,
			index,
			persisted, preferencesHint);
		ShapeStyle style = (ShapeStyle)view.getStyle(NotationPackage.eINSTANCE.getShapeStyle());
		style.setFillColor((FigureUtilities.colorToInteger(LogicColorConstants.connectorGreen)).intValue());
		style.setLineColor((FigureUtilities.colorToInteger(LogicColorConstants.logicBlack)).intValue());
		return view;
	}	
}
