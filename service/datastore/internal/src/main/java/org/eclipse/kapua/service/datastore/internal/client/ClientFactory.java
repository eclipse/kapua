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
package org.eclipse.kapua.service.datastore.internal.client;

import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;

/**
 * Datastore client factory. It returns the singleton client instance.
 *
 * @since 1.0
 */
public class ClientFactory {

    private final static String CANNOT_LOAD_CLIENT_ERROR_MSG = "Cannot load the provided client class name [%s]. Check the configuration.";
    private final static String CLIENT_CLASS_NAME;
    private static Class<DatastoreClient> INSTANCE;

    static {
        DatastoreSettings config = DatastoreSettings.getInstance();
        CLIENT_CLASS_NAME = config.getString(DatastoreSettingKey.CONFIG_CLIENT_CLASS);
    }

    /**
     * Return the client instance. The implementation is specified by {@link DatastoreSettingKey#CONFIG_CLIENT_CLASS}.
     * 
     * @return
     * @throws ClientUnavailableException
     */
    @SuppressWarnings("unchecked")
    public static DatastoreClient getInstance() throws ClientUnavailableException {
        //lazy synchronization
        if (INSTANCE == null) {
            synchronized (CLIENT_CLASS_NAME) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = (Class<DatastoreClient>) Class.forName(CLIENT_CLASS_NAME);
                    } catch (ClassNotFoundException e) {
                        throw new ClientUnavailableException(String.format(CANNOT_LOAD_CLIENT_ERROR_MSG, CLIENT_CLASS_NAME), e);
                    }
                }
            }
        }
        try {
            DatastoreClient c = INSTANCE.newInstance();
            c.setModelContext(new ModelContextImpl());
            c.setQueryConverter(new QueryConverterImpl());
            return c;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ClientUnavailableException(String.format(CANNOT_LOAD_CLIENT_ERROR_MSG, CLIENT_CLASS_NAME), e);
        }
    }

}
