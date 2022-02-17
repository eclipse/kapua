/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

/**
 * Enumeration of {@link JpaExecutionInstanceDataFields}.
 *
 * @since 1.2.0
 */
public enum JpaExecutionInstanceDataFields {
    JOB_EXEC_ID,
    JOB_INSTANCE_ID,
    CREATE_TIME,
    START_TIME,
    END_TIME,
    UPDATE_TIME,
    PARAMETERS,
    BATCH_STATUS,
    EXIT_STATUS
}
