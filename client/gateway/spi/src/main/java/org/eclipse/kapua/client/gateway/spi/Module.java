/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.spi;

public interface Module {

    public default void initialize(final ModuleContext context) {
    }

    public default void applicationAdded(final String applicationId) {
    }

    public default void applicationRemoved(final String applicationId) {
    }

    public default void connected() {
    }

    public default void disconnected() {
    }
}
