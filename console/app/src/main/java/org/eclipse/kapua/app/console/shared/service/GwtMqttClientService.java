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
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.commons.client.GwtKapuaException;
import org.eclipse.kapua.app.console.commons.shared.model.GwtXSRFToken;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mqtt")
public interface GwtMqttClientService extends RemoteService {

    /**
     * subscribes to the specified topics - auto connects the MQTT client if necessary
     *
     * @param brokerAddress   the URL of the broker
     * @param clientId    the client ID
     * @param topics    the topics to subscribe to
     * @throws GwtKapuaException  if the initialization fails
     */
    public void subscribe(GwtXSRFToken xsfrToken, String brokerAddress, String clientId, String[] topics) throws GwtKapuaException;

    /**
     * unsubscribes to the specified topics
     *
     * @param brokerAddress   the URL of the broker
     * @param clientId    the client ID
     * @param topics    the topics to unsubscribe from
     * @throws GwtKapuaException  if the initialization fails
     */
    public void unsubscribe(GwtXSRFToken xsfrToken, String brokerAddress, String clientId, String[] topics) throws GwtKapuaException;
}
