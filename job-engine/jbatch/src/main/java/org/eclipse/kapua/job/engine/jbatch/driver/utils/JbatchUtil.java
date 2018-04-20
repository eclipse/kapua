/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.driver.utils;

import org.eclipse.kapua.job.engine.jbatch.driver.JbatchDriver;
import org.eclipse.kapua.job.engine.jbatch.setting.KapuaJobEngineSetting;
import org.eclipse.kapua.job.engine.jbatch.setting.KapuaJobEngineSettingKeys;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import java.util.Date;
import java.util.Timer;

public class JbatchUtil {

    private static final KapuaJobEngineSetting JOB_ENGINE_SETTING = KapuaJobEngineSetting.getInstance();

    private static final JobOperator JOB_OPERATOR = BatchRuntime.getJobOperator();

    private JbatchUtil() {
    }

    public static void waitForStop(JobExecution jobExecution, Runnable nextTask) {
        long maxWait = JOB_ENGINE_SETTING.getLong(KapuaJobEngineSettingKeys.JOB_ENGINE_STOP_WAIT_CHECK_TIME_MAX);
        int checkInterval = JOB_ENGINE_SETTING.getInt(KapuaJobEngineSettingKeys.JOB_ENGINE_STOP_WAIT_CHECK_TIME_INTERVAL);

        String timerName = new StringBuilder().append(JbatchDriver.class.getSimpleName())
                .append("-")
                .append(WaitForJobExecutionStopTask.class.getSimpleName())
                .append("-")
                .append(jobExecution.getJobName())
                .append("-")
                .append(jobExecution.getExecutionId())
                .toString();

        Timer waitForJobStopTimer = new Timer(timerName, false);

        waitForJobStopTimer.scheduleAtFixedRate(
                new WaitForJobExecutionStopTask(
                        jobExecution,
                        new Date(new Date().getTime() + maxWait),
                        waitForJobStopTimer,
                        nextTask),
                0,
                checkInterval);
    }

}
