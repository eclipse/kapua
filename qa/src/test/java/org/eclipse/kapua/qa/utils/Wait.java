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
package org.eclipse.kapua.qa.utils;

import static java.time.Duration.ofMillis;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;

public final class Wait {

    private Wait() {
    }

    @FunctionalInterface
    public interface Condition {

        public boolean test() throws Exception;
    }

    /**
     * Test for a specific amount of time for a state of become {@code true}
     * <p>
     * In case the state provider or the {@link Thread#sleep(long)} throws an exception the method
     * will wrap this into a {@link RuntimeException} rethrow it.
     * </p>
     * 
     * @param duration
     *            the duration to wait
     * @param testPeriod
     *            the period between tests
     * @param condition
     *            The condition to test for
     * @return {@code true} if the state was reached in that period of time, {@code false} otherwise
     */
    public static boolean testFor(final Duration duration, final Duration testPeriod, final Condition condition) {
        requireNonNull(duration);
        requireNonNull(testPeriod);
        requireNonNull(condition);

        final Instant end = now().plus(duration.abs());
        while (now().isBefore(end)) {
            try {
                final boolean result = condition.test();
                if (result)
                    return true;
                Thread.sleep(testPeriod.toMillis());
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean testFor(final Duration duration, final Condition condition) {
        return testFor(duration, ofMillis(100), condition);
    }

    /**
     * Wait for a condition or fail
     * 
     * @param duration
     *            the duration to wait for
     * @param testPeriod
     *            the period between testing for the condition
     * @param condition
     *            the condition to test for
     * @throws WaitTimeoutException
     *             in case the wait timed out
     */
    public static void waitFor(final Duration duration, final Duration testPeriod, final Condition condition) throws WaitTimeoutException {
        if (testFor(duration, testPeriod, condition)) {
            return;
        }
        throw new WaitTimeoutException(duration);
    }

    /**
     * Wait for a condition or fail
     * 
     * @param action
     *            short description what this waits for. This will be used in the exception message.
     * @param duration
     *            the duration to wait for
     * @param testPeriod
     *            the period between testing for the condition
     * @param condition
     *            the condition to test for
     * @throws WaitTimeoutException
     *             in case the wait timed out
     */
    public static void waitFor(final String action, final Duration duration, final Duration testPeriod, final Condition condition) throws WaitTimeoutException {
        if (testFor(duration, testPeriod, condition)) {
            return;
        }
        throw new WaitTimeoutException(action, duration);
    }
}
