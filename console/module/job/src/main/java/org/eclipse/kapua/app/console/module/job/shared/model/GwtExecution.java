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
package org.eclipse.kapua.app.console.module.job.shared.model;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.Date;

public class GwtExecution extends GwtUpdatableEntityModel {

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("startedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getStartedOn()));
        } else if ("endedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getEndedOn()));
        } else {
            return super.get(property);
        }
    }

    public Date getStartedOn() {
        return get("startedOn");
    }

    public String getStartedOnFormatted() {
        return get("StartedOnFormatted");
    }

    public void setStartedOn(Date startedOn) {
        set("startedOn", startedOn);
    }

    public Date getEndedOn() {
        return get("endedOn");
    }

    public String getEndedOnFormatted() {
        return get("EndedOnFormatted");
    }

    public void setEndedOn(Date endedOn) {
        set("endedOn", endedOn);
    }
}
