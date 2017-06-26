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
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.commons.utils.DateUtils;
import org.eclipse.kapua.app.console.client.util.MessageUtils;
import org.eclipse.kapua.app.console.commons.shared.models.KapuaBaseModel;

public class GwtSnapshot extends KapuaBaseModel implements Serializable {
    private static final long serialVersionUID = 204571826084819719L;


    public GwtSnapshot() {
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X get(String property) {
        if ("createdOnFormatted".equals(property)) {
            if (((Date) get("createdOn")).getTime() == 0) {
                return (X) (MessageUtils.get("snapSeeded"));
            }
            return (X) (DateUtils.formatDateTime((Date) get("createdOn")));
        } else if ("snapshotId".equals(property)) {
            return (X) Long.valueOf(((Date) get("createdOn")).getTime());
        } else {
            return super.get(property);
        }
    }

    public Date getCreatedOn() {
        return (Date) get("createdOn");
    }

    public long getSnapshotId() {
        return ((Date) get("createdOn")).getTime();
    }

    public String getCreatedOnFormatted() {
        return DateUtils.formatDateTime((Date) get("createdOn"));
    }

    public void setCreatedOn(Date createdOn) {
        set("createdOn", createdOn);
    }
}
