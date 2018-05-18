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
package org.eclipse.kapua.app.console.module.api.shared.eventMessages;

import com.google.gwt.event.shared.GwtEvent;

public class MessageReceivedEvent extends GwtEvent<MessageReceivedEventHandler> {
    public static Type<MessageReceivedEventHandler> type = new Type<MessageReceivedEventHandler>();
    private final String message;

    public MessageReceivedEvent(String message) {
        this.message = message;
    }

    @Override
    public Type<MessageReceivedEventHandler> getAssociatedType() {
        return type;
    }

    @Override
    protected void dispatch(MessageReceivedEventHandler handler) {
        handler.onMessageReceived(this);
    }

    public String getMessage() {
        return message;
    }
}
