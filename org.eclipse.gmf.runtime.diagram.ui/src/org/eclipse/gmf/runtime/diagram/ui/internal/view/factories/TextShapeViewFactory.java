/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.view.factories;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.ui.services.parser.CommonParserHint;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * The TextShapeView Factory class 
 * @author mmostafa
 * @canBeSeenBy %level1
 */
public class TextShapeViewFactory
	extends AbstractShapeViewFactory {

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractNodeView#decorateView(org.eclipse.gmf.runtime.diagram.ui.internal.view.IContainerView,
	 *      org.eclipse.core.runtime.IAdaptable, java.lang.String, int, boolean)
	 */
	protected void decorateView(View containerView, View view,
			IAdaptable semanticAdapter, String semanticHint, int index,
			boolean persisted) {
		super.decorateView(containerView, view, semanticAdapter, semanticHint,
			index, persisted);

		getViewService().createNodeView(semanticAdapter, view,
			Properties.DIAGRAM_NAME, ViewUtil.APPEND, persisted, getPreferencesHint());

		getViewService().createNodeView(semanticAdapter, view,
			CommonParserHint.DESCRIPTION, ViewUtil.APPEND, persisted, getPreferencesHint());
	}

	/**
	 * @return a list of style for the newly created view or an empty list if none (do not return null)
	 */
	protected List createStyles() {
		List styles = super.createStyles();
		styles.add(NotationFactory.eINSTANCE.createShapeStyle());
		return styles;
	}
}