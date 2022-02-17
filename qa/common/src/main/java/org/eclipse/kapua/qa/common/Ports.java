/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
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
package org.eclipse.kapua.qa.common;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

public final class Ports {

    private Ports() {
    }

    public static boolean isPortOpen(int port) throws IOException {
        try (final ServerSocket socket = new ServerSocket(port)) {
            return false;
        } catch (BindException e) {
            return true;
        }
    }
}
