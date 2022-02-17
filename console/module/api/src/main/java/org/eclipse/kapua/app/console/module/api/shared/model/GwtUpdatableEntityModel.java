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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.util.Date;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;

public abstract class GwtUpdatableEntityModel extends GwtEntityModel {

    private static final long serialVersionUID = -8268981197541435575L;

    public GwtUpdatableEntityModel() {
        super();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("modifiedOnFormatted".equals(property)) {
            if (getModifiedOn() != null) {
                return (X) (DateUtils.formatDateTime(getModifiedOn()));
            } else {
                return (X) "N/A";
            }
        } else {
            return super.get(property);
        }
    }

    public Date getModifiedOn() {
        return get("modifiedOn");
    }

    public String getModifiedOnFormatted() {
        return get("modifiedOnFormatted");
    }

    public void setModifiedOn(Date modifiedOn) {
        set("modifiedOn", modifiedOn);
    }

    public String getModifiedBy() {
        return get("modifiedBy");
    }

    public void setModifiedBy(String modifiedBy) {
        set("modifiedBy", modifiedBy);
    }

    public String getModifiedByName() {
        return get("modifiedByName");
    }

    public void setModifiedByName(String modifiedByName) {
        set("modifiedByName", modifiedByName);
    }

    public int getOptlock() {
        return get("optlock");
    }

    public void setOptlock(int optlock) {
        set("optlock", optlock);
    }
}
