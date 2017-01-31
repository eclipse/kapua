/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.model;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * {@link KapuaObjectFactory} definition.<br>
 * This interface is a marker interface and it must be applied to all model object factory that needs
 * service discovery.
 * 
 * @see {@link KapuaLocator#getFactory(Class)}
 * @since 1.0.0
 *
 */
public interface KapuaObjectFactory {
}
