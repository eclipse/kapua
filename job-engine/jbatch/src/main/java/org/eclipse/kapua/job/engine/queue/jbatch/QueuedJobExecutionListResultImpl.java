/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.queue.jbatch;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;

/**
 * {@link QueuedJobExecutionListResult} implementation.
 *
 * @since 1.1.0
 */
public class QueuedJobExecutionListResultImpl extends KapuaListResultImpl<QueuedJobExecution> implements QueuedJobExecutionListResult {

}
