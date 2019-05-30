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
package org.eclipse.kapua.service.job.engine.app;

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class JobEngineModule extends SimpleModule {

    public JobEngineModule() {
        setMixInAnnotation(JobStartOptions.class, JobStartOptionMixin.class);
        setMixInAnnotation(KapuaId.class, KapuaIdMixin.class);
    }
}
