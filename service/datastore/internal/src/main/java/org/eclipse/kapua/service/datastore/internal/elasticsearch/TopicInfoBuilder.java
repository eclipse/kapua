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

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.TopicInfoImpl;
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

public class TopicInfoBuilder {
	
	private TopicInfoImpl topicInfo;
	
	public TopicInfoBuilder buildFromTopic(SearchHit searchHit) throws KapuaInvalidTopicException, ParseException, KapuaIllegalNullArgumentException {
		
		ArgumentValidator.notNull(searchHit, "SearchHit must not be null.");
		
		Map<String, SearchHitField> fields = searchHit.getFields();
		String id = searchHit.getId();
		String topicName = fields.get(EsSchema.TOPIC_SEM_NAME).getValue();
		String lastMsgId = fields.get(EsSchema.TOPIC_MESSAGE_ID).getValue();
		String lastMsgTimestampStr = fields.get(EsSchema.TOPIC_TIMESTAMP).getValue();
		String asset = fields.get(EsSchema.TOPIC_ASSET).getValue();
		String account = fields.get(EsSchema.TOPIC_ACCOUNT).getValue();
		
		KapuaTopic topic = new KapuaTopic(String.format("%s/%s/%s", account, asset, topicName));
		Date timestamp = (Date)EsUtils.convertToKapuaObject("date", lastMsgTimestampStr);
		topicInfo = new TopicInfoImpl(account, new StorableIdImpl(id));
		topicInfo.setFullTopicName(topic.getFullTopic());
		topicInfo.setLastMessageId(new StorableIdImpl(lastMsgId));
		topicInfo.setLastMessageTimestamp(timestamp);
		return this;
	}

	public TopicInfo getTopicInfo() {
		return this.topicInfo;
	}
}
