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
package org.eclipse.kapua.qa.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Help tracking {@link AutoCloseable}s during initialization phases
 * <p>
 * The idea of {@link Starting} is to provide a tracker which will free
 * resources if the startup fails. And let go of them once the method
 * {@link #started()} got called.
 * </p>
 * <p>
 * Assume the following code:
 * </p>
 * 
 * <pre><code>
class MyClass {

public MyClass () {
   this.resource1 = new Resource1();
   this.resource2 = new Resource2(resource1);
}

public void close () {
  try ( Suppressed{@code <Exception>} s = Suppressed.withException() ) {
    s.closeSuppressed(resource2);
    s.closeSuppressed(resource1);
  }
}}
 * </code></pre>
 * <p>
 * If something fails during initialization of MyClass then {@code resource1} would not get
 * closed properly. This class can help in solving this scenario by using:
 * </p>
 * 
 * <pre><code>
public MyClass ()  {
  try ( Starting s = new Starting() )  {
    this.resource1 = new Resource1();
    s.add ( this.resource1 );
    this.resource2 = new Resource2(resource1);
    s.add ( this.resource2 );

    s.started(); // from now on this class takes on control
  }
}}}
 * </code></pre>
 * <p>
 * If something happens during the initialization of resource2, then the try-with-resource construct
 * call {@link Starting#close()} which will close all added closables up to this point. Once the
 * {@link #started()} method was called, this will be reset and the caller takes over responsibility
 * of closing this.
 * </p>
 */
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

        // when we get closed but the started() method was not called

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
        return result != null ? result : Collections.emptyList();
    }

}
