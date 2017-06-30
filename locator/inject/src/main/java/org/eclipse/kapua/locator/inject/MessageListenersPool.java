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

import java.util.List;

/**
 * The Managed object pool keeps track of the managed objects been created
 * by the dependency injecton and make them available to the client classes.
 *  
 * @since 1.0
 */
public interface MessageListenersPool {

    public void add(Object object);
    public <T> List<T> getImplementationsOf(Class<T> clazz);
    public void register(PoolListener listener);
    public PoolListener deregister(PoolListener listener);
}
