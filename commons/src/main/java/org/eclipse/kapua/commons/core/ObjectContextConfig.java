/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import com.google.inject.AbstractModule;

/**
 * Provides bindings used by {@link ObjectContext} to create beans.
 * <p>
 * Application implementations may extend this class to provide the 
 * actual bindings that.
 * 
 */
public class ObjectContextConfig extends AbstractModule {

    @Override
    protected void configure() {
    }

}
