/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.security;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test the correctness of the doPrivilege calls.
 * 
 * @since 1.0
 *
 */
public class KapuaDoPrivilegeTest {

    private static Logger logger = LoggerFactory.getLogger(KapuaDoPrivilegeTest.class);

    private final static int MAX_EXECUTION = 10;
    private final static int CONCURRENT_THREAD = 20;
    private final static int FIX_WAIT_TIME_FOR_EXECUTION_END = 30000;

    private static long maxRandomWait;

    @Test
    /**
     * Test the correctness of the doPrivilege for nested calls.
     * It performs MAX_EXECUTION nested calls end check the correctness of the KapuaSession by checking if the instance id before and after the nested call is the same.
     */
    public void testNestedDoPrivilege() throws Exception {
        maxRandomWait = 0;

        (new DoPrivilegeCallable("noMultiThread")).invokeCode();
    }

    @Test
    /**
     * Test the correctness of the doPrivilege for nested calls spanned between few threads. All threads startup is scheduled at the same time.
     * It performs MAX_EXECUTION nested calls end check the correctness of the KapuaSession by checking if the instance id before and after the nested call is the same.
     */
    public void testNestedDoPrivilegeMultiThreadSync() throws Exception {
        maxRandomWait = 100;
        ScheduledExecutorService es = Executors.newScheduledThreadPool(CONCURRENT_THREAD);
        long executionTimeOut = MAX_EXECUTION * maxRandomWait + FIX_WAIT_TIME_FOR_EXECUTION_END;
        List<ScheduledFuture<Void>> doPrivilegeList = new ArrayList<ScheduledFuture<Void>>();
        for (int i = 0; i < CONCURRENT_THREAD; i++) {
            doPrivilegeList.add(es.schedule(new DoPrivilegeCallable("sync_random_wait_" + i), 0, TimeUnit.MILLISECONDS));
        }
        waitForTermination(doPrivilegeList, executionTimeOut);

        maxRandomWait = 0;
        es.shutdown();
        es = Executors.newScheduledThreadPool(CONCURRENT_THREAD);
        executionTimeOut = MAX_EXECUTION * maxRandomWait + FIX_WAIT_TIME_FOR_EXECUTION_END;
        doPrivilegeList.clear();
        for (int i = 0; i < CONCURRENT_THREAD; i++) {
            doPrivilegeList.add(es.schedule(new DoPrivilegeCallable("sync_no_random_wait_" + i), 0, TimeUnit.MILLISECONDS));
        }
        waitForTermination(doPrivilegeList, executionTimeOut);
    }

    @Test
    /**
     * Test the correctness of the doPrivilege for nested calls spanned between few threads. Threads startup delay is spanned between 0 and 200msec and between 0 and 100msec.
     * It performs MAX_EXECUTION nested calls end check the correctness of the KapuaSession by checking if the instance id before and after the nested call is the same.
     */
    public void testNestedDoPrivilegeMultiThreadNoSync() throws Exception {
        maxRandomWait = 100;
        ScheduledExecutorService es = Executors.newScheduledThreadPool(CONCURRENT_THREAD);
        long executionTimeOut = MAX_EXECUTION * maxRandomWait + FIX_WAIT_TIME_FOR_EXECUTION_END;
        List<ScheduledFuture<Void>> doPrivilegeList = new ArrayList<ScheduledFuture<Void>>();
        for (int i = 0; i < CONCURRENT_THREAD; i++) {
            long delay = (long) (Math.random() * 200d);
            logger.debug("Delay: ", new Object[] { delay });
            doPrivilegeList.add(es.schedule(new DoPrivilegeCallable("nosync_random_wait_" + i), delay, TimeUnit.MILLISECONDS));
        }
        waitForTermination(doPrivilegeList, executionTimeOut);

        maxRandomWait = 0;
        es.shutdown();
        es = Executors.newScheduledThreadPool(CONCURRENT_THREAD);
        executionTimeOut = MAX_EXECUTION * maxRandomWait + FIX_WAIT_TIME_FOR_EXECUTION_END;
        doPrivilegeList.clear();
        for (int i = 0; i < CONCURRENT_THREAD; i++) {
            long delay = (long) (Math.random() * 100d);
            logger.debug("Delay: ", new Object[] { delay });
            doPrivilegeList.add(es.schedule(new DoPrivilegeCallable("nosync_no_random_wait_" + i), delay, TimeUnit.MILLISECONDS));
        }
        waitForTermination(doPrivilegeList, executionTimeOut);
    }

    private void waitForTermination(List<ScheduledFuture<Void>> scheduledList, long timeOut) throws InterruptedException {
        boolean done = true;
        long maxAttempt = timeOut / 200;
        int attempt = 0;
        do {
            done = true;
            for (ScheduledFuture<Void> scheduled : scheduledList) {
                if (!scheduled.isDone()) {
                    done = false;
                    break;
                }
            }
            Thread.sleep(200);
            Assert.assertTrue("Timeout waiting for execution [" + timeOut + " msec]. The task is not yet completed!", attempt++ <= maxAttempt);
        } while (!done);
        Assert.assertTrue("The task is not yet completed!", done);
    }

    private String getKapuaSessionId() {
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        if (kapuaSession != null) {
            return kapuaSession.toString();
        } else {
            return null;
        }
    }

    private class DoPrivilegeCallable implements Callable<Void> {

        private String callableName;

        public DoPrivilegeCallable(String callableName) {
            this.callableName = callableName;
        }

        @Override
        public Void call() throws Exception {
            return doPrivilegeCode(0);
        }

        public Void invokeCode() throws Exception {
            return doPrivilegeCode(0);
        }

        private Void doPrivilegeCode(final Integer executionProgress) throws Exception {
            logger.debug(callableName + ": DoPriviledge call {}", executionProgress.intValue());
            String originalKapuaSessionId = getKapuaSessionId();

            if (executionProgress < MAX_EXECUTION) {
                long wait = (long) (Math.random() * maxRandomWait);
                logger.debug(callableName + " Wait: ", new Object[] { wait });
                Thread.sleep(wait);
                KapuaSecurityUtils.doPrivileged(() -> doPrivilegeCode(Integer.valueOf(executionProgress.intValue() + 1)));
            }

            String kapuaSessionId = getKapuaSessionId();

            logger.debug("Execution: {} - KapuaSession object ID before {} - and after {} the nested DoPriviledge call",
                    new Object[] { executionProgress.intValue(), originalKapuaSessionId, kapuaSessionId });
            Assert.assertEquals("Wrong session ID!!! The do priledge method corrupted the KapuaSession", kapuaSessionId, originalKapuaSessionId);
            return (Void) null;
        }

    }
}
