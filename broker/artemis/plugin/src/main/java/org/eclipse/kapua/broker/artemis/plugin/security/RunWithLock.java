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

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to run code encapsulated into a lock/unlock block
 *
 */
public class RunWithLock {

    private static Logger logger = LoggerFactory.getLogger(RunWithLock.class);

    //TODO make it configurable?
    //TODO how many threads are available?
    private static final int LOCKS_SIZE = 128;
    private static final Lock[] LOCKS = new Lock[LOCKS_SIZE];

    static {
        //init lock array
        for (int i=0; i<LOCKS_SIZE; i++) {
            LOCKS[i] = new ReentrantLock(true);
        }
    }

    private RunWithLock() {
    }

    /**
     * Gets a Lock based on provided key value and run the callable inside a lock/unlock block
     * Given a key values the acquired Lock is always the same so this method is doing a synchonization based on key value.
     * @param <T>
     * @param key
     * @param callable
     * @return
     * @throws Exception
     */
    public static <T> T run(String key, Callable<T> callable) throws Exception {
        Lock lock = getLock(key);
        try {
            long start = System.currentTimeMillis();
            lock.lock();
            logger.info("lock acquired in {}msec", System.currentTimeMillis() - start);
            return callable.call();
        }
        finally {
            lock.unlock();
        }
    }

    private static Lock getLock(String connectionId) {
        return LOCKS[Math.abs(connectionId.hashCode()%LOCKS_SIZE)];
    }

}
