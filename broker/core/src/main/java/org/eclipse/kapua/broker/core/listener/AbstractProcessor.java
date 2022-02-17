/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
