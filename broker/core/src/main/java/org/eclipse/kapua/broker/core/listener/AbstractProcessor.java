/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.core.listener;

import org.eclipse.kapua.KapuaException;

/**
 * Default camel pojo endpoint processor.
 *
 * @param <M>
 *            Message to process
 * 
 * @since 1.0
 */
public abstract class AbstractProcessor<M> extends AbstractListener {

    /**
     * Creates a processor with the specified name (used by component metrics name)
     * 
     * @param name
     */
    protected AbstractProcessor(String name) {
        super("processor", name);
    }

    /**
     * Process the incoming message
     * 
     * @param message
     *            incoming message
     * @throws KapuaException
     */
    public abstract void processMessage(M message) throws KapuaException;

}
