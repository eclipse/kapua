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
package org.eclipse.kapua.commons.core.vertx;

public class EventBusServerResponseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1005656055006273926L;

    public EventBusServerResponseException(int code) {
        super();
        this.code = code;
    }

    public EventBusServerResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public EventBusServerResponseException(int code, String message, Throwable t) {
        super(message, t);
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }    
}
