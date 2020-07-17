/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.datastore.internal.converter.ModelContextImpl;
import org.eclipse.kapua.service.datastore.internal.converter.QueryConverterImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.elasticsearch.client.DatastoreClient;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientInitializationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Datastore client factory. It returns the singleton client instance.<br>
 * The datastore client is instantiated by reflection using class implementation provided by {@link DatastoreSettingKey#CONFIG_CLIENT_CLASS}
 *
 * @since 1.0
 */
public class DatastoreClientFactory {

    private static final String CLIENT_CLASS_NAME;
    private static DatastoreClient instance;

    static {
        DatastoreSettings config = DatastoreSettings.getInstance();
        CLIENT_CLASS_NAME = config.getString(DatastoreSettingKey.CONFIG_CLIENT_CLASS);
    }

    private DatastoreClientFactory() {
    }

    /**
     * Gets the Elasticsearch client instance.
     * The implementation is specified by {@link DatastoreSettingKey#CONFIG_CLIENT_CLASS}.
     *
     * @return An Elasticsearch client.
     * @throws ClientInitializationException if error occurs if the {@link DatastoreClient} cannot be initialized
     */
    public static DatastoreClient getInstance() throws ClientInitializationException {
        //lazy synchronization
        if (instance == null) {
            synchronized (DatastoreClientFactory.class) {
                if (instance == null) {
                    Class<DatastoreClient> datastoreClientInstance;
                    try {
                        datastoreClientInstance = (Class<DatastoreClient>) Class.forName(CLIENT_CLASS_NAME);
                    } catch (ClassNotFoundException e) {
                        throw new ClientInitializationException(e, CLIENT_CLASS_NAME);
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
                        throw new ClientInitializationException(e, CLIENT_CLASS_NAME);
                    }
                }
            }
        }
        return instance;
    }

    /**
     * Closes the client instance
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
