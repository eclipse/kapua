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
package org.eclipse.kapua.app.console.module.job.shared.model.scheduler;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.Date;
import java.util.List;

public class GwtTrigger extends GwtUpdatableEntityModel {

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("startsOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getStartsOn()));
        } else if ("endsOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getEndsOn()));
        } else {
            return super.get(property);
        }
    }

    public String getTriggerName() {
        return get("triggerName");
    }

    public void setTriggerName(String triggerName) {
        set("triggerName", triggerName);
    }

    public Date getStartsOn() {
        return get("startsOn");
    }

    public String getStartsOnFormatted() {
        return get("startsOnFormatted");
    }

    public void setStartsOn(Date startsOn) {
        set("startsOn", startsOn);
    }

    public Date getEndsOn() {
        return get("endsOn");
    }

    public Date getEndsOnFormatted() {
        return get("endsOnFormatted");
    }

    public void setEndsOn(Date endsOn) {
        set("endsOn", endsOn);
    }

    public String getCronScheduling() {
        return get("cronScheduling");
    }

    public void setCronScheduling(String cronScheduling) {
        set("cronScheduling", cronScheduling);
    }

    public Long getRetryInterval() {
        return get("retryInterval");
    }

    public void setRetryInterval(Long retryInterval) {
        set("retryInterval", retryInterval);
    }

    public <P extends GwtTriggerProperty> List<P> getTriggerProperties() {
        return get("triggerProperties");
    }

    public void setTriggerProperties(List<GwtTriggerProperty> triggerProperties) {
        set("triggerProperties", triggerProperties);
    }

    public String getJobId() {
        for (GwtTriggerProperty property : getTriggerProperties()) {
            if (property.getPropertyName().equals("jobId")) {
                return property.getPropertyValue();
            }
        }
        return null;
    }
}
