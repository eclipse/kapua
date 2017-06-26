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


public class EventContextImpl implements EventContext {

    private String id;
    
    private EventContextImpl(String id) {
        this.id = id;
    }
    
    public static EventContextImpl fromId(String id) {
        return new EventContextImpl(id);
    }
    
    @Override
    public String getId() {
        return id;
    }
}
