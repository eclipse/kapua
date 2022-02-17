/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app;

import org.eclipse.kapua.kura.simulator.payload.Message;

public interface Handler extends AutoCloseable {

    public default void connected() {
    }

    public default void disconnected() {
    }

    public default void processMessage(final Message message) {
    }

    @Override
    public default void close() throws Exception {
    }
}
