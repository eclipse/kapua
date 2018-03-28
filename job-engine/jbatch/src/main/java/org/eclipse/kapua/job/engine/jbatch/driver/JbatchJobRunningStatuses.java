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
package org.eclipse.kapua.job.engine.jbatch.driver;

import com.google.common.collect.Lists;

import javax.batch.runtime.BatchStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * This {@link List} identifies the running statuses of a Jbatch job.
 * <p>
 * {@link List} contains {@link BatchStatus}es:
 * <ul>
 * <li>BatchStatus.STARTED</li>
 * <li>BatchStatus.STARTING</li>
 * <li>BatchStatus.STOPPING</li>
 * </ul>
 * <p>
 * They can be retrieved as a singleton with {@link #getStatuses()}
 *
 * @since 1.0.0
 */
public class JbatchJobRunningStatuses extends ArrayList<BatchStatus> implements List<BatchStatus> {

    private static final JbatchJobRunningStatuses STATUSES = new JbatchJobRunningStatuses();

    private JbatchJobRunningStatuses() {
        addAll(Lists.newArrayList(BatchStatus.STARTED, BatchStatus.STARTING, BatchStatus.STOPPING));
    }

    public static JbatchJobRunningStatuses getStatuses() {
        return STATUSES;
    }
}
