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

import org.eclipse.kapua.job.engine.jbatch.driver.JbatchJobRunningStatuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WaitForJobExecutionStopTask extends TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(WaitForJobExecutionStopTask.class);
    private static final JobOperator JOB_OPERATOR = BatchRuntime.getJobOperator();

    private JobExecution jobExecution;
    private Date expireDate;
    private Timer timerTask;
    private Runnable nextTask;

    public WaitForJobExecutionStopTask(JobExecution jobExecution, Date expireDate, Timer timerTask, Runnable nextTask) {
        this.jobExecution = jobExecution;
        this.expireDate = expireDate;
        this.timerTask = timerTask;
        this.nextTask = nextTask;
    }

    @Override
    public void run() {
        JobExecution runningJobExecution = JOB_OPERATOR.getJobExecution(jobExecution.getExecutionId());

        if (JbatchJobRunningStatuses.getStatuses().contains(runningJobExecution.getBatchStatus())) {

            if (new Date().before(expireDate)) {
                LOG.info("Job {} not yet stopped...", runningJobExecution.getJobName());
            } else {
                LOG.error("Job {} did stopped in time! Expire date was: {}", runningJobExecution.getJobName(), expireDate);
                synchronized (timerTask) {
                    timerTask.cancel();
                }
            }
        } else {
            LOG.info("Job {} stopped!", runningJobExecution.getJobName());

            try {
                LOG.info("Executing next action...");
                synchronized (timerTask) {
                    timerTask.cancel();
                }

                nextTask.run();
                LOG.info("Executing next action... DONE!");
            } catch (Exception t) {
                LOG.info("Executing next action... ERROR!", t);
            }
        }
    }

}
