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
package org.eclipse.kapua.job.engine.queue;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link QueuedJobExecutionFactory} definition.
 *
 * @since 1.1.0
 */
public interface QueuedJobExecutionFactory extends KapuaEntityFactory<QueuedJobExecution, QueuedJobExecutionCreator, QueuedJobExecutionQuery, QueuedJobExecutionListResult> {

}
