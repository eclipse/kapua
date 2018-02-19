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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

import java.util.Date;
import java.util.List;

public class GwtTriggerCreator extends GwtEntityCreator {

    private String triggerName;
    private Date startsOn;
    private Date endsOn;
    private String cronScheduling;
    private Long retryInterval;
    private List<GwtTriggerProperty> triggerProperties;

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Date getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(Date startsOn) {
        this.startsOn = startsOn;
    }

    public Date getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(Date endsOn) {
        this.endsOn = endsOn;
    }

    public String getCronScheduling() {
        return cronScheduling;
    }

    public void setCronScheduling(String cronScheduling) {
        this.cronScheduling = cronScheduling;
    }

    public Long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    public List<GwtTriggerProperty> getTriggerProperties() {
        return triggerProperties;
    }

    public void setTriggerProperties(List<GwtTriggerProperty> triggerProperties) {
        this.triggerProperties = triggerProperties;
    }
}
