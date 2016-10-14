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

import org.elasticsearch.common.xcontent.XContentBuilder;

public class EsMetricDocumentBuilder {
	
	private String id;
	private XContentBuilder content;
	
	public String getId() {
		return id;
	}
	public XContentBuilder getContent() {
		return content;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setContent(XContentBuilder content) {
		this.content = content;
	}

}
