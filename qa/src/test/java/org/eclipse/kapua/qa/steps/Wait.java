/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import static java.time.Instant.now;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;

public final class Wait {

    private Wait() {
    }

    /**
     * For a specific amount of time for a state of become {@code true}
     * <p>
     * In case the state provider or the {@link Thread#sleep(long)} throws an exception the method
     * will wrap this into a {@link RuntimeException} rethrow it.
     * </p> 
     * @param duration
     *            the duration to wait
     * @param state
     *            the state provider
     * @return {@code true} if the state was reached in that period of time, {@code false} otherwise
     */
    public static boolean waitFor(final Duration duration, final Callable<Boolean> state) {
        final Instant end = now().plus(duration.abs());
        while (now().isBefore(end)) {
            try {
                final Boolean result = state.call();
                if (Boolean.TRUE.equals(result)) {
                    return true;
                }
                Thread.sleep(100);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
