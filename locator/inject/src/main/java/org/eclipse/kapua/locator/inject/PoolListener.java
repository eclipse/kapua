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
package org.eclipse.kapua.locator.inject;

/**
 * Implementers of this interface can register with a ManagedObjectPool instance and
 * get notified when a new object is added in the pool.
 * 
 * @since 1.0
 */
public interface PoolListener {

    public void onObjectAdded(Object object);
}
