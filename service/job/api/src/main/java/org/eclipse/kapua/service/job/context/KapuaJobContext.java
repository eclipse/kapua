/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.context;

import javax.batch.runtime.context.JobContext;

import org.eclipse.kapua.model.id.KapuaId;

public interface KapuaJobContext extends JobContext {

    public KapuaId getScopeId();

    public KapuaId getJobId();

    public void setKapuaExecutionId(KapuaId executionId);

    public KapuaId getKapuaExecutionId();

}
