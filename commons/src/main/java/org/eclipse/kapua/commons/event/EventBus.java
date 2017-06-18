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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListener;

/**
 * @since 0.3.0
 */
public interface EventBus {
    
    public void publish(KapuaEvent event);
    public void subscribe(KapuaEventListener eventListener);
    public KapuaEventListener unsubscribe(KapuaEventListener eventListener);
    public boolean isSubscribed(KapuaEventListener eventListener);
}
