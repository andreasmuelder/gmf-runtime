/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.listener;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.gmf.runtime.diagram.core.internal.listener.ModelServerListener;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A model server listener that broadcast EObject events to
 * all registered listeners.
 * 
 * @author melaasar, mmostafa
 */
public class PresentationListener extends ModelServerListener {
	/** The PresentationListener singleton */
	private static PresentationListener instance;

	/**
	 * gives access to the <code>PresentationListener</code> singleton
	 * @return the <code>PresentationListener</code> singleton
	 */
	public static PresentationListener getInstance() {
		if ( instance == null ) {
			instance = new PresentationListener();
		}
		return instance;
	}
	
	/**
	 * gets a notifier to the given element.
	 * @param element the element to get the notifier for
	 * @return the Notifier of the element
	 * @deprecated the PropertyChangeNotifier is deprecated, to add a listener use the 
	 * <code>addPropertyChangeListener</code> Api instead.
	 */
	public static PropertyChangeNotifier getNotifier(EObject element) {
		return PresentationListener.getInstance().getPropertyChangeNotifier(element);
	}
	
	/**
	 * gets a notifier to the given element.
	 * @param element the element to get the notifier for
	 * @return the Notifier of the element
	 * @deprecated the PropertyChangeNotifier is deprecated, to add a listener use the 
	 * <code>addPropertyChangeListener</code> Api instead.
	 */
	public static PropertyChangeNotifier getNotifier(EObject element,EStructuralFeature feature) {
		return PresentationListener.getInstance().getPropertyChangeNotifier(element,feature);
	}

	/**
	 * Wraps the supplied event into a property change event.
	 * @param event	the event top wrap
	 * @return	the Notification event
	 */
	protected NotificationEvent getNotificationEvent( Notification event ) {
		Object notifier = event.getNotifier();
		if (notifier instanceof View) {
			View view = (View)notifier;
			if (view != null)
				return new NotificationEvent( 
					view, 
					event.getOldValue(),
					event.getNewValue(),
					event);
		}
		return new NotificationEvent(event);
	}

	/**
	 * gets a subset of all the registered listeners who are interested
	 * in receiving the supplied event.
	 * @param event the event to use
	 * @return	the interested listeners in the event
	 */
	protected Set getInterestedListeners( Notification event ) {
		Set listenerSet = super.getInterestedListeners(event);

		EObject notifier = (EObject) event.getNotifier();
		if (notifier instanceof EAnnotation){
			addListenersOfNotifier(listenerSet, notifier.eContainer(), event);
		} else if (!(notifier instanceof View)) {
			while (notifier != null && !(notifier instanceof View)) {
				notifier = notifier.eContainer();
			}
			addListenersOfNotifier(listenerSet, notifier, event);
		}
		return listenerSet;
	}

	/**
	 * Helper method to add all the listners of the given <code>notifier</code>
	 * to the list of listeners
	 * @param listenerSet
	 * @param notifier
	 */
	private void addListenersOfNotifier(Set listenerSet, EObject notifier,Notification event) {
		if (notifier != null) {
			Collection c = getListeners(notifier, event.getFeature());
			if (c != null) {
				if (listenerSet.isEmpty())
					listenerSet.addAll(c);
				else {
					Iterator i = c.iterator();
					while (i.hasNext()) {
						Object o = i.next();
						listenerSet.add(o);
					}
				}
			}
		}
	}
	
    /**
     * Forwards the event to all interested  listeners.
     * @param event the event to handle
	 */
	protected void handleElementEvent(Notification event) {
		MEditingDomain doamin = null;
		if (!event.isTouch()
			&& !(doamin = MEditingDomainGetter.getMEditingDomain(event))
				.isUndoNotification(event) && !doamin.isRedoNotification(event)) {
			EObject element = (EObject) event.getNotifier();
			while (element != null && !(element instanceof View)) {
				element = element.eContainer();
			}
			if (element != null) {
				ViewUtil.persistElement((View) element);
			}
		}
		super.handleElementEvent(event);
	}
}
