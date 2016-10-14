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
import org.eclipse.kapua.service.datastore.internal.model.AssetInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

public class AssetInfoBuilder {

	private AssetInfoImpl assetInfo;
	
	public AssetInfoBuilder build(SearchHit searchHit) throws ParseException, KapuaIllegalNullArgumentException {
		
		ArgumentValidator.notNull(searchHit, "searchHit");
		
		Map<String, SearchHitField> fields = searchHit.getFields();
		String idStr = searchHit.getId();
		String assetName = fields.get(EsSchema.ASSET_NAME).getValue();
		String timestampStr = fields.get(EsSchema.ASSET_TIMESTAMP).getValue();
		String scope = fields.get(EsSchema.ASSET_ACCOUNT).getValue();
		this.assetInfo = new AssetInfoImpl(scope, new StorableIdImpl(idStr));
		this.assetInfo.setAsset(assetName);
        this.assetInfo.setLastMessageTimestamp((Date) EsUtils.convertToKapuaObject("date", timestampStr));
        // TODO FIll the other fields
		return this;
	}
	
	public AssetInfo getAssetInfo() {
		return this.assetInfo;
	}
}
