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
package org.eclipse.kapua.app.console.module.api.shared.eventMessages.handlers;

import org.eclipse.kapua.app.console.module.api.shared.eventMessages.MessageChecker;
import org.eclipse.kapua.app.console.module.api.shared.eventMessages.MessageReceivedEvent;
import org.eclipse.kapua.app.console.module.api.shared.eventMessages.MessageReceivedEventHandler;

public class UserDisplayNameMessageHandler implements MessageReceivedEventHandler {
    private static UserDisplayNameMessageHandler instance;
    private static MessageChecker checker = new MessageChecker();

    public UserDisplayNameMessageHandler() {}

    public static UserDisplayNameMessageHandler getInstance(){
        if(instance == null){
            instance = new UserDisplayNameMessageHandler();
        }
        return instance;
    }

    public static MessageChecker getChecker(){
        return checker;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String newMessage = event.getMessage();
        checker.newMessageReceived(newMessage);
    }
}
