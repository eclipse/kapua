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
package org.eclipse.kapua.job.engine.jbatch;

import com.ibm.jbatch.container.services.impl.DelegatingBatchArtifactFactoryImpl;
import org.eclipse.kapua.locator.KapuaLocator;

public class KapuaDelegatingBatchArtifactFactoryImpl extends DelegatingBatchArtifactFactoryImpl {

    private final KapuaLocatorInjector kapuaLocatorInjector = new KapuaLocatorInjector(KapuaLocator.getInstance());

    @Override
    public Object load(String batchId) {
        final Object loadedArtifact = super.load(batchId);
        kapuaLocatorInjector.injectKapuaReferences(loadedArtifact);
        return loadedArtifact;
    }
}
