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
package org.eclipse.kapua.service.datastore.internal.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum DatastoreSettingKey implements SettingKey
{

    ELASTICSEARCH ("datastore.elasticsearch"),
    ELASTICSEARCH_NODES ("datastore.elasticsearch.node"),
    ELASTICSEARCH_CLUSTER("datastore.elasticsearch.cluster"),
    ELASTICSEARCH_TOPIC_MAX_DEPTH("datastore.elasticsearch.topic.max.depth"),
    ELASTICSEARCH_IDX_REFRESH_INTERVAL("datastore.elasticsearch.index.refresh_interval"),

    CONFIG_CACHE_LOCAL_EXPIRE_AFTER("datastore.cache.local.expire.after"),
    CONFIG_CACHE_LOCAL_SIZE_MAXIMUM("datastore.cache.local.size.maximum"),
    CONFIG_TOPIC_MAX_DEPTH("datastore.elasticsearch.topic.max.depth"),
    CONFIG_DATA_STORAGE_ENABLE_TIMING_PROFILE("datastore.enableTimingProfile"),
    CONFIG_DATA_STORAGE_TIMING_PROFILE_THRESHOLD("datastore.timingProfileThreshold");
    
	private String key;
	
	private DatastoreSettingKey(String key) {
		this.key = key;
	}
	
	@Override
	public String key() {
		return key;
	}
}
