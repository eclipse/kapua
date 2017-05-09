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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.eclipse.kapua.commons.util.ThrowingRunnable;

/**
 * Helper class for working with suppressed {@link Exception}s
 * <p>
 * This class should be used in a try-with-resources block, adding
 * exceptions using the {@link #add(Throwable)} method, or exceptions
 * while running code with {@link #run(ThrowingRunnable)} or {@link #call(Callable)} or
 * a failing close with {@link #closeSuppressed(AutoCloseable)} will add those
 * exceptions to the internal list
 * </p>
 * <p>
 * When the instance is closed by the try-with-resources {@link #close()} call and
 * there are recorded exceptions, then the close call will throw an exception as well.
 * </p>
 * <p>
 * The thrown exception is of type {@code <X>}. When throwing an exception it tries
 * not to pollute the exception stack with unnecessary wrapper exceptions. In case multiple
 * exceptions where added, then they will be added as suppressed exceptions to the first one.
 * If the first one is not of type {@code <X>}, then it will be wrapped in a new instance
 * of type {@code <X>} and suppressed exceptions will be added to this new instance instead.
 * </p>
 * 
 * @param <X>
 *            The exception class this instance will throw
 */
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

    public void call(final Callable<?> callable) {
        if (callable == null) {
            return;
        }

        try {
            callable.call();
        } catch (Exception e) {
            add(e);
        }
    }

    public void add(Throwable e) {
        if (e == null) {
            return;
        }

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
        return new Suppressed<>(RuntimeException.class, RuntimeException::new);
    }

    public static void closeAll(Collection<? extends AutoCloseable> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        closeAll(list.stream());
    }

    public static void closeAll(Stream<? extends AutoCloseable> list) {
        if (list == null) {
            return;
        }

        try (final Suppressed<RuntimeException> s = withRuntimeException()) {
            list.forEach(s::closeSuppressed);
        }
    }

}
