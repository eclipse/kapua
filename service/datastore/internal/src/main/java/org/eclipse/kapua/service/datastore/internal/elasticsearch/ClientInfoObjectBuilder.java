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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

/**
 * Client information object builder.<br>
 * This object converts the schema coming from an Elasticsearch search hit to a Kapua client information object (unmarshal).
 *
 * @since 1.0.0
 */
public class ClientInfoObjectBuilder {

    private ClientInfoImpl clientInfo;

    /**
     * Build a {@link ClientInfoObjectBuilder} from the Elasticsearch search hit
     *
     * @param searchHit
     * @return
     * @throws EsObjectBuilderException
     * @since 1.0.0
     */
    public ClientInfoObjectBuilder build(SearchHit searchHit)
            throws EsObjectBuilderException {
        String idStr = searchHit.getId();

        Map<String, SearchHitField> fields = searchHit.getFields();
        KapuaId scopeId = KapuaEid.parseCompactId(fields.get(ClientInfoField.SCOPE_ID.field()).getValue());
        String clientId = fields.get(ClientInfoField.CLIENT_ID.field()).getValue();
        String timestampStr = fields.get(ClientInfoField.TIMESTAMP.field()).getValue();
        String messageId = fields.get(ClientInfoField.MESSAGE_ID.field()).getValue();

        this.clientInfo = new ClientInfoImpl(scopeId, new StorableIdImpl(idStr));
        this.clientInfo.setClientId(clientId);
        this.clientInfo.setFirstMessageId(new StorableIdImpl(messageId));

        Date timestamp = (Date) EsUtils.convertToKapuaObject("date", timestampStr);
        this.clientInfo.setFirstMessageOn(timestamp);

        return this;
    }

    /**
     * Get the built Kapua client information object
     *
     * @return
     * @since 1.0.0
     */
    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }
}
