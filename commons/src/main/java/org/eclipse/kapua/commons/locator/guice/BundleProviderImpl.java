/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.locator.guice;

import java.util.Set;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.Bundle;
import org.eclipse.kapua.commons.locator.BundleProvider;


/**
 * @since 0.3.0
 *
 */
public class BundleProviderImpl implements BundleProvider {

    @Inject Set<Bundle> bundles;
    
    @Override
    public Set<Bundle> getBundles() {
        return bundles;
    }

}
