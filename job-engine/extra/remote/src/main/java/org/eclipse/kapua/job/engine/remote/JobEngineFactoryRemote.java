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
package org.eclipse.kapua.job.engine.remote;

import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaProvider;

/**
 * {@link JobEngineFactory} implementation.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class JobEngineFactoryRemote implements JobEngineFactory {

    @Override
    public JobStartOptions newJobStartOptions() {
        return new JobStartOptionsRemote();
    }
}
