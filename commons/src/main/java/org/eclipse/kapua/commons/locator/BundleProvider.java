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
package org.eclipse.kapua.commons.locator;

import java.util.Set;

import org.eclipse.kapua.commons.core.Bundle;

/**
 * Bundle provider retrieves the list of bundles
 * 
 * @since 0.3.0
 */
public interface BundleProvider {

    public Set<Bundle> getBundles();
}
