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
