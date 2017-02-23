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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.net.UnknownHostException;
import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EsClient {
	
	@SuppressWarnings("unused")
	private static final Logger   s_logger = LoggerFactory.getLogger(EsClient.class);
			
	private static Client client;
	
	private static final int DEFAULT_PORT = 9300;

	public static Client getcurrent() throws UnknownHostException, EsDatastoreException {
		// TODO read hostname and port from configurations
		if (client == null) {
			DatastoreSettings config = DatastoreSettings.getInstance();
			String esHost = System.getenv("ELASTICSEARCH_SERVICE_HOST");
			int esPort = DEFAULT_PORT;

			if(esHost == null) {
				Map<String, String> map = config.getMap(String.class, DatastoreSettingKey.ELASTICSEARCH_NODES, "[0-9]+");
				String[] esNodes = new String[]{};
				if (map != null)
					esNodes = map.values().toArray(new String[]{});

				if (esNodes == null || esNodes.length == 0)
					throw new EsDatastoreException("No elasticsearch nodes found");

				String[] nodeParts = getNodeParts(esNodes[0]);

				if (nodeParts.length > 0)
					esHost = nodeParts[0];
				if (nodeParts.length > 1) {
					try {
						Integer.parseInt(nodeParts[1]);
					} catch (NumberFormatException e) {
						throw new EsDatastoreException("Could not parse port: " + nodeParts[1]);
					}
				}
			}
			client = EsUtils.getEsClient(esHost, esPort, config.getString(DatastoreSettingKey.ELASTICSEARCH_CLUSTER));
		}
		return client;
	}
	
	private static String[] getNodeParts(String node)
	{
	    if (node==null)
	        return new String[] {};
	    
	    String[] split = node.split(":");
	    return split;
	}
}
