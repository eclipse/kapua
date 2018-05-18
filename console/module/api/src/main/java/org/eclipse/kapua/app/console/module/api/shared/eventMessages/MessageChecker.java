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
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class MessageChecker implements HasHandlers {
    private HandlerManager handlerManager;

    public MessageChecker() {
        handlerManager = new HandlerManager(this);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    public HandlerRegistration addMessageReceivedEventHandler(
            MessageReceivedEventHandler handler) {
        return handlerManager.addHandler(MessageReceivedEvent.type, handler);
    }

    public void newMessageReceived(String displayName) {
        MessageReceivedEvent event = new MessageReceivedEvent(displayName);
        fireEvent(event);
    }
}
