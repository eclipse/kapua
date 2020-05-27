/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
