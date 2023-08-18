/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Helper class to run code encapsulated into a lock/unlock block
 */
public class RunWithLock {

    //TODO make it configurable?
    //TODO how many threads are available?
    private static final int LOCKS_SIZE = 128;
    private final Lock[] locks;

    @Inject
    public RunWithLock() {
        //init lock array
        locks = new Lock[LOCKS_SIZE];
        for (int i = 0; i < LOCKS_SIZE; i++) {
            locks[i] = new ReentrantLock(true);
        }
    }

    /**
     * Gets a Lock based on provided key value and run the callable inside a lock/unlock block
     * Given a key values the acquired Lock is always the same so this method is doing a synchonization based on key value.
     *
     * @param <T>
     * @param key
     * @param callable
     * @return
     * @throws Exception
     */
    public <T> T run(String key, Callable<T> callable) throws Exception {
        Lock lock = getLock(key);
        try {
            lock.lock();
            return callable.call();
        } finally {
            lock.unlock();
        }
    }

    private Lock getLock(String connectionId) {
        return locks[Math.abs(connectionId.hashCode() % LOCKS_SIZE)];
    }

}
