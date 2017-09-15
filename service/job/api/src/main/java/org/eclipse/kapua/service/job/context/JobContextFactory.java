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
import javax.batch.runtime.context.StepContext;

import org.eclipse.kapua.model.KapuaObjectFactory;

public interface JobContextFactory extends KapuaObjectFactory {

    public KapuaJobContext newJobContext(JobContext jobContext);

    public KapuaStepContext newStepContext(StepContext stepContext);
}
