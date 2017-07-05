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
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.commons.shared.model.Enum;
import org.eclipse.kapua.app.console.commons.shared.model.KapuaBaseModel;

public class GwtDomain extends KapuaBaseModel implements IsSerializable, Comparable<GwtDomain>,Enum {

    private String domainName;

    //        access_info, //
    //        access_token, //
    //        account, //
    //        broker, //
    //        credential, //
    //        datastore, //
    //        device, //
    //        device_connection, //
    //        device_event, //
    //        device_lifecycle, //
    //        device_management, //
    //        domain, //
    //        group, //
    //        role, //
    //        user, //
    //
    //        ALL; //

    public GwtDomain(String domainName) {
        set("domainName", domainName);
    }

    public GwtDomain() {
    }

    /**
     * @return the name of this domain
     * @since 1.0.0
     */
    public String getDomainName() {
        return get("domainName");
    }

    public void setDomainName(String domainName) {
        set("domainName", domainName);
    }

    @Override
    public int compareTo(GwtDomain o) {
        return getDomainName().compareToIgnoreCase(o.getDomainName());
    }

    @Override
    public String name() {
        return getDomainName();
    }
}