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
package org.eclipse.kapua.service.simulator.steps;

import java.util.LinkedList;
import java.util.List;

public class Starting implements AutoCloseable {

    private List<AutoCloseable> closeables;

    public void add(AutoCloseable closeable) {
        if (closeable != null) {
            if (closeables == null) {
                closeables = new LinkedList<>();
            }
            closeables.add(closeable);
        }
    }

    @Override
    public void close() throws Exception {

        if (closeables == null) {
            return;
        }

        final LinkedList<Exception> exceptions = new LinkedList<>();

        for (final AutoCloseable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty()) {
            final Exception e = exceptions.pollFirst();
            exceptions.forEach(e::addSuppressed);
            throw e;
        }
    }

    public List<AutoCloseable> started() {
        final List<AutoCloseable> result = this.closeables;
        this.closeables = null;
        return result;
    }

}
