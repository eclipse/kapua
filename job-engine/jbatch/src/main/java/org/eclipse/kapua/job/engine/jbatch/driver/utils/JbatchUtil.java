/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.driver.utils;

import org.eclipse.kapua.job.engine.jbatch.driver.JbatchDriver;
import org.eclipse.kapua.job.engine.jbatch.setting.JobEngineSetting;
import org.eclipse.kapua.job.engine.jbatch.setting.JobEngineSettingKeys;

import javax.batch.runtime.JobExecution;
import java.util.Date;
import java.util.Timer;

public class JbatchUtil {

    private static final JobEngineSetting JOB_ENGINE_SETTING = JobEngineSetting.getInstance();

    private JbatchUtil() {
    }

    public static void waitForStop(JobExecution jobExecution, Runnable nextTask) {
        long maxWait = JOB_ENGINE_SETTING.getLong(JobEngineSettingKeys.JOB_ENGINE_STOP_WAIT_CHECK_TIME_MAX);
        int checkInterval = JOB_ENGINE_SETTING.getInt(JobEngineSettingKeys.JOB_ENGINE_STOP_WAIT_CHECK_TIME_INTERVAL);

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
