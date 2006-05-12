/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.action;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.workspace.AbstractEMFOperation;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;
import org.eclipse.gmf.runtime.common.ui.action.IActionWithProgress;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIDebugOptions;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIPlugin;
import org.eclipse.gmf.runtime.emf.ui.internal.MslUIStatusCodes;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The abstract parent of all concrete action handlers that execute model
 * commands. Concrete subclasses must provide a definition of the
 * <code>doRun()</code> method to gather any required input and execute a model
 * command.
 * 
 * @author khussey
 */
public abstract class AbstractModelActionHandler
	extends AbstractActionHandler {
    
    private IStatus status;

	/**
	 * Constructs a new model action handler for the specified workbench part.
	 * 
	 * @param workbenchPart The workbench part to which this model action
	 *                       handler applies.
	 */
	protected AbstractModelActionHandler(IWorkbenchPart workbenchPart) {
		super(workbenchPart);
	}

	/**
	 * Constructs a new model action handler for the specified workbench page.
	 * 
	 * @param workbenchPage The workbench page to which this model action
	 *                       handler applies.
	 */
	protected AbstractModelActionHandler(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

	/**
	 * Runs this model action handler as a read action.
	 * 
	 * @see IActionWithProgress#run(IProgressMonitor)
	 */
	public final void run(final IProgressMonitor progressMonitor) {
        
        if (isReadOnly()) {
            // run exclusive so that subclasses can only read from the model
            try {
                getEditingDomain().runExclusive(new Runnable() {

                    public void run() {
                        AbstractModelActionHandler.super.run(progressMonitor);
                    }
                });
            } catch (InterruptedException e) {
                Trace
                    .catching(MslUIPlugin.getDefault(),
                        MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                        "run", e); //$NON-NLS-1$
            }

        } else {
            // run in an EMF operation so that subclasses can both read and
            // write to the model
            AbstractEMFOperation operation = new AbstractEMFOperation(
                getEditingDomain(), getLabel()) {

                protected IStatus doExecute(IProgressMonitor monitor,
                        IAdaptable info)
                    throws ExecutionException {

                    AbstractModelActionHandler.super.run(progressMonitor);
                    return getStatus();
                };
            };

            try {
                getActionManager().getOperationHistory().execute(operation,
                    new NullProgressMonitor(), null);

            } catch (ExecutionException e) {
                Trace
                    .catching(MslUIPlugin.getDefault(),
                        MslUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                        "run", e); //$NON-NLS-1$
                Log.error(MslUIPlugin.getDefault(),
                    MslUIStatusCodes.IGNORED_EXCEPTION_WARNING, e
                        .getLocalizedMessage(), e);
            }
        }
        
	}
    
    /**
     * Gets my editing domain. Can be <code>null</code> if I don't modify
     * model resources.
     * 
     * @return my editing domain.
     */
    protected abstract TransactionalEditingDomain getEditingDomain();
    
    /**
     * Answers whether or not I am a read-only action. Returns
     * <code>false</code> by default. Subclasses may override.
     * 
     * @return <code>false</code>
     */
    protected boolean isReadOnly() {
        return false;
    }
    
    /**
     * Gets the status of running this action delegate.
     * 
     * @return my status
     */
    protected IStatus getStatus() {
        if (status == null) {
            return Status.OK_STATUS;
        }
        return status;
    }
    
    /**
     * Sets the action delegate status. Subclasses should set the status when they
     * execute a command.
     * 
     * @param status
     *            the new status
     */
    protected void setStatus(IStatus status) {
        this.status = status;
    }

}