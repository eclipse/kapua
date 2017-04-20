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

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import org.eclipse.kapua.commons.util.ThrowingRunnable;

public class Suppressed<X extends Exception> implements AutoCloseable {

    @FunctionalInterface
    public static interface ExceptionSupplier<X> {

        public X createNew(Throwable cause) throws Exception;
    }

    private final Class<X> clazz;
    private final ExceptionSupplier<X> supplier;
    private LinkedList<Throwable> errors;

    public Suppressed(final Class<X> clazz) {
        this.clazz = clazz;
        this.supplier = cause -> {
            Constructor<X> ctor = clazz.getConstructor(Throwable.class);
            return ctor.newInstance(cause);
        };
    }

    public Suppressed(final Class<X> clazz, final ExceptionSupplier<X> supplier) {
        this.clazz = clazz;
        this.supplier = supplier;
    }

    public void closeSuppressed(AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            return;
        }

        run(autoCloseable::close);
    }

    public void run(final ThrowingRunnable runnable) {
        if (runnable == null) {
            return;
        }

        try {
            runnable.run();
        } catch (Exception e) {
            add(e);
        }
    }

    public void run(final Callable<?> callable) {
        if (callable == null)
            return;

        try {
            callable.call();
        } catch (Exception e) {
            add(e);
        }
    }

    public void add(Throwable e) {
        if (e == null)
            return;

        if (errors == null) {
            errors = new LinkedList<>();
        }
        errors.add(e);
    }

    @Override
    public void close() throws X {
        if (errors == null || errors.isEmpty()) {
            // no recorded errors
            return;
        }

        X result;
        final Throwable first = errors.pollFirst();
        if (clazz.isAssignableFrom(first.getClass())) {
            // matching type, no need to wrap
            result = clazz.cast(first);
        } else {
            // wrap
            result = newException(first);
        }

        errors.forEach(result::addSuppressed);
        errors.clear();

        throw result;
    }

    private X newException(final Throwable cause) {
        try {
            return supplier.createNew(cause);
        } catch (Exception e) {
            throw new Error("Unable to create instance of Exception class", e);
        }
    }

    public static Suppressed<Exception> withException() {
        return new Suppressed<>(Exception.class, Exception::new);
    }

    public static Suppressed<RuntimeException> withRuntimeException() {
        return new Suppressed<>(Exception.class, RuntimeException::new);
    }

}
