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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.client.util.DateUtils;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtAlert extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 3970841019638997878L;

    public enum GwtSeverity implements IsSerializable {
        CRITICAL, WARNING, INFO;
        GwtSeverity() {
        }
    }

    public GwtAlert() {
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("createdOnFormatted".equals(property)) {
            return (X) DateUtils.formatDateTime(getCreatedOn());
        } else if ("severityEnum".equals(property)) {
            String severity = getSeverity();
            if (severity != null)
                return (X) (GwtSeverity.valueOf(severity));
            return (X) "";
        } else {
            return super.get(property);
        }
    }

    public String getAccount() {
        return (String) get("account");
    }

    public void setAccount(String account) {
        set("account", account);
    }

    public Date getCreatedOn() {
        return (Date) get("createdOn");
    }

    public String getCreatedOnFormatted() {
        return (String) get("createdOnFormatted");
    }

    public void setCreatedOn(Date createdOn) {
        set("createdOn", createdOn);
    }

    public long getCreatedBy() {
        return ((Long) get("createdBy")).longValue();
    }

    public void setCreatedBy(long createdBy) {
        set("createdBy", createdBy);
    }

    public String getSource() {
        return (String) get("source");
    }

    public String getUnescapedSource() {
        return (String) getUnescaped("source");
    }

    public void setSource(String source) {
        set("source", source);
    }

    public String getSeverity() {
        return (String) get("severity");
    }

    public GwtSeverity getSeverityEnum() {
        return GwtSeverity.valueOf(getSeverity());
    }

    public void setSeverity(String severity) {
        set("severity", severity);
    }

    public String getCategory() {
        return (String) get("category");
    }

    public void setCategory(String category) {
        set("category", category);
    }

    public String getCode() {
        return (String) get("code");
    }

    public void setCode(String code) {
        set("code", code);
    }

    public String getMessage() {
        return (String) get("message");
    }

    public String getMessageHtmlSafe() {
        return get("messageHtmlSafe");
    }

    public void setMessage(String message) {
        set("message", message);
    }

    public byte[] getAttachment() {
        return (byte[]) get("attachment");
    }

    public void setAttachment(byte[] attachment) {
        set("attachment", attachment);
    }

    public String getUuid() {
        return (String) get("uuid");
    }

    public void setUuid(String uuid) {
        set("uuid", uuid);
    }
}
