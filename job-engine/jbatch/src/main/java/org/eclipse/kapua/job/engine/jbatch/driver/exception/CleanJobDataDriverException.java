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
package org.eclipse.kapua.job.engine.jbatch.driver.exception;

public class CleanJobDataDriverException extends JbatchDriverException {
    public CleanJobDataDriverException(Throwable t, String jobName) {
        super(JbatchDriverErrorCodes.CANNOT_CLEAN_JOB_DATA, t, jobName);
    }
}
