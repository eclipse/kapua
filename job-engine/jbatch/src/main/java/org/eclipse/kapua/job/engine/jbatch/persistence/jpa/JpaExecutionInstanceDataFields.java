/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

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
