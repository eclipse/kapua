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

import org.eclipse.kapua.app.console.commons.shared.model.KapuaBaseModel;

import java.io.Serializable;

public class GwtSettings extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7285859217584861659L;

    public void setAdminPasswordCurrent(String pwdCurrent) {
        set("pwdCurrent", pwdCurrent);
    }

    public String getAdminPasswordCurrent() {
        return (String) get("pwdCurrent");
    }

    public void setAdminPasswordNew(String pwdNew) {
        set("pwdNew", pwdNew);
    }

    public String getAdminPasswordNew() {
        return (String) get("pwdNew");
    }

    public String getSMTPServer() {
        return (String) get("smtpServer");
    }

    public void setSMTPServer(String value) {
        set("smtpServer", value);
    }

    public int getSMTPPort() {
        return ((Integer) get("smtpPort"));
    }

    public void setSMTPPort(int value) {
        set("smtpPort", value);
    }

    public String getSMTPUsername() {
        return (String) get("smtpUsername");
    }

    public void setSMTPUsername(String value) {
        set("smtpUsername", value);
    }

    public String getSMTPPassword() {
        return (String) get("smtpPassword");
    }

    public void setSMTPPassword(String value) {
        set("smtpPassword", value);
    }

    public String getSMTPFromAddress() {
        return (String) get("smtpFromAddress");
    }

    public void setSMTPFromAddress(String value) {
        set("smtpFromAddress", value);
    }

    public boolean getSMTPUseTLS() {
        return (Boolean) get("smtpUseTLS");
    }

    public void setSMTPUseTLS(boolean value) {
        set("smtpUseTLS", value);
    }

    public boolean getSMTPUseSSL() {
        return (Boolean) get("smtpUseSSL");
    }

    public void setSMTPUseSSL(boolean value) {
        set("smtpUseSSL", value);
    }
}
