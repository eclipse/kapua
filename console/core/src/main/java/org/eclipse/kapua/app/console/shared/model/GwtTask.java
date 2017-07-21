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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.commons.client.util.DateUtils;
import org.eclipse.kapua.app.console.commons.shared.model.KapuaBaseModel;

public class GwtTask extends KapuaBaseModel implements Serializable {
    private static final long serialVersionUID = -1665104446637357480L;

    public GwtTask() {
    }

    public Long getId() {
        return (Long) get("id");
    }

    public void setId(Long id) {
        set("id", id);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X get(String property) {
        if ("createdOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime((Date) get("createdOn")));
        } else if ("startedOnFormatted".equals(property)) {
            if (get("startedOn") != null) {
                return (X) (DateUtils.formatDateTime((Date) get("startedOn")));
            } else {
                return (X) "";
            }
        } else if ("completedOnFormatted".equals(property)) {
            if (get("completedOn") != null) {
                return (X) (DateUtils.formatDateTime((Date) get("completedOn")));
            } else {
                return (X) "";
            }
        } else if ("lastStepOnFormatted".equals(property)) {
            if (get("lastStepOn") != null) {
                return (X) (DateUtils.formatDateTime((Date) get("lastStepOn")));
            } else {
                return (X) "";
            }
        } else {
            return super.get(property);
        }
    }

    public Long getAccountId() {
        return (Long) get("accountId");
    }

    public void setAccountId(Long accountId) {
        set("accountId", accountId);
    }

    public String getName() {
        return (String) get("name");
    }

    public String getUnescapedName() {
        return (String) getUnescaped("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public Date getCreatedOn() {
        return (Date) get("createdOn");
    }

    public String getCreatedOnFormatted() {
        return DateUtils.formatDateTime((Date) get("createdOn"));
    }

    public void setCreatedOn(Date createdOn) {
        set("createdOn", createdOn);
    }

    public long getCreatedBy() {
        return (Long) get("createdBy");
    }

    public void setCreatedBy(long createdBy) {
        set("createdBy", createdBy);
    }

    public String getCreatedByFormatted() {
        return (String) get("createdByFormatted");
    }

    public void setCreatedByFormatted(String createdByFormatted) {
        set("createdByFormatted", createdByFormatted);
    }

    public Date getStartedOn() {
        return (Date) get("startedOn");
    }

    public String getStartedOnFormatted() {
        return DateUtils.formatDateTime((Date) get("startedOn"));
    }

    public void setStartedOn(Date startedOn) {
        set("startedOn", startedOn);
    }

    public Date getCompletedOn() {
        return (Date) get("completedOn");
    }

    public String getCompletedOnFormatted() {
        return DateUtils.formatDateTime((Date) get("completedOn"));
    }

    public void setCompletedOn(Date completedOn) {
        set("completedOn", completedOn);
    }

    public String getStatus() {
        return (String) get("status");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getTaskClassname() {
        return (String) get("taskClassname");
    }

    public void setTaskClassname(String taskClassName) {
        set("taskClassname", taskClassName);
    }

    public String getLastStepInfo() {
        return (String) get("lastStepInfo");
    }

    public void setLastStepInfo(String lastStepInfo) {
        set("lastStepInfo", lastStepInfo);
    }

    public Date getLastStepOn() {
        return (Date) get("lastStepOn");
    }

    public String getLastStepOnFormatted() {
        return DateUtils.formatDateTime((Date) get("lastStepOn"));
    }

    public void setLastStepOn(Date lastStepOn) {
        set("lastStepOn", lastStepOn);
    }

    public String getLog() {
        return (String) get("log");
    }

    public String getUnescapedLog() {
        return (String) getUnescaped("log");
    }

    public void setLog(String log) {
        set("log", log);
    }
}
