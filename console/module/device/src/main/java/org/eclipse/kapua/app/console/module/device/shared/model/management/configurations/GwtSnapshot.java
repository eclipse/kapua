/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.model.management.configurations;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.client.util.MessageUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.Date;

public class GwtSnapshot extends KapuaBaseModel implements Serializable {
    private static final long serialVersionUID = 204571826084819719L;

    private static final String CREATED_ON = "createdOn";

    public GwtSnapshot() {
    }

    @Override
    public <X> X get(String property) {
        if ("createdOnFormatted".equals(property)) {
            if (((Date) get(CREATED_ON)).getTime() == 0) {
                return (X) (MessageUtils.get("snapSeeded"));
            }
            return (X) (DateUtils.formatDateTime((Date) get(CREATED_ON)));
        } else if ("snapshotId".equals(property)) {
            return (X) Long.valueOf(((Date) get(CREATED_ON)).getTime());
        } else {
            return super.get(property);
        }
    }

    public Date getCreatedOn() {
        return (Date) get(CREATED_ON);
    }

    public long getSnapshotId() {
        return ((Date) get(CREATED_ON)).getTime();
    }

    public String getCreatedOnFormatted() {
        return DateUtils.formatDateTime((Date) get(CREATED_ON));
    }

    public void setCreatedOn(Date createdOn) {
        set(CREATED_ON, createdOn);
    }
}
