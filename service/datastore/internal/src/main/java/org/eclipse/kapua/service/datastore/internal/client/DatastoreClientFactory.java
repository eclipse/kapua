/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;

/**
 * Datastore client factory. It returns the singleton client instance.<br>
 * The datastore client is instantiated by reflection using class implementation provided by {@link DatastoreSettingKey#CONFIG_CLIENT_CLASS}
 *
 * @since 1.0
 */
public class DatastoreClientFactory {

    private final static String CANNOT_LOAD_CLIENT_ERROR_MSG = "Cannot load the provided client class name [%s]. Check the configuration.";
    private final static String CLIENT_CLASS_NAME;
    private static Class<DatastoreClient> datastoreClientInstance;
    private static DatastoreClient instance;

    static {
        DatastoreSettings config = DatastoreSettings.getInstance();
        CLIENT_CLASS_NAME = config.getString(DatastoreSettingKey.CONFIG_CLIENT_CLASS);
    }

    private DatastoreClientFactory() {
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
        if (instance == null) {
            synchronized (DatastoreClientFactory.class) {
                if (instance == null) {
                    try {
                        datastoreClientInstance = (Class<DatastoreClient>) Class.forName(CLIENT_CLASS_NAME);
                    } catch (ClassNotFoundException e) {
                        throw new ClientUnavailableException(String.format(CANNOT_LOAD_CLIENT_ERROR_MSG, CLIENT_CLASS_NAME), e);
                    }
                    try {
                        // this is a cleaner way to instatiate the client
                        // return INSTANCE.getConstructor(ModelContext.class, QueryConverter.class).newInstance(new ModelContextImpl(), new QueryConverterImpl());
                        // but in that way who implements the interface is not advised to expose a constructor with the 2 needed parameters
                        // so I prefer to instantiate the object using the empty constructor then setting the converter using the setters (provided by the interface)
                        // instance = datastoreClientinstance.newInstance();

                        Method getInstanceMethod = datastoreClientInstance.getMethod("getInstance", new Class[0]);
                        instance = (DatastoreClient) getInstanceMethod.invoke(null, new Object[0]);
                        instance.setModelContext(new ModelContextImpl());
                        instance.setQueryConverter(new QueryConverterImpl());
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        throw new ClientUnavailableException(String.format(CANNOT_LOAD_CLIENT_ERROR_MSG, CLIENT_CLASS_NAME), e);
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Close the client instance
     * 
     * @throws ClientException
     */
    public static void close() throws ClientException {
        if (instance != null) {
            synchronized (DatastoreClientFactory.class) {
                if (instance != null) {
                    try {
                        instance.close();
                    } finally {
                        instance = null;
                    }
                }
            }
        }
    }

}
