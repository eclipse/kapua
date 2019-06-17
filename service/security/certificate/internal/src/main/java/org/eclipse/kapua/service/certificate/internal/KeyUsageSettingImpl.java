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
package org.eclipse.kapua.service.certificate.internal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.eclipse.kapua.service.certificate.KeyUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;

import java.util.Objects;

@Embeddable
public class KeyUsageSettingImpl implements KeyUsageSetting {

    @Enumerated(EnumType.STRING)
    @Column(name = "key_usage")
    private KeyUsage keyUsage;

    @Basic
    @Column(name = "allowed")
    private boolean allowed;

    @Basic
    @Column(name = "kapua_allowed")
    private Boolean kapuaAllowed;

    protected KeyUsageSettingImpl() {
    }

    public KeyUsageSettingImpl(KeyUsageSetting keyUsageSetting) {
        setKeyUsage(keyUsageSetting.getKeyUsage());
        setAllowed(keyUsageSetting.getAllowed());
        setKapuaAllowed(keyUsageSetting.getKapuaAllowed());
    }

    public KeyUsageSettingImpl(KeyUsage keyUsage, boolean allowed, Boolean kapuaAllowed) {
        this.keyUsage = keyUsage;
        this.allowed = allowed;
        this.kapuaAllowed = kapuaAllowed;
    }

    @Override
    public KeyUsage getKeyUsage() {
        return keyUsage;
    }

    @Override
    public void setKeyUsage(KeyUsage keyUsage) {
        this.keyUsage = keyUsage;
    }

    @Override
    public boolean getAllowed() {
        return allowed;
    }

    @Override
    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public Boolean getKapuaAllowed() {
        return kapuaAllowed;
    }

    @Override
    public void setKapuaAllowed(Boolean kapuaAllowed) {
        this.kapuaAllowed = kapuaAllowed;
    }

    public static KeyUsageSettingImpl parse(KeyUsageSetting keyUsageSetting) {
        return keyUsageSetting != null ? keyUsageSetting instanceof KeyUsageSettingImpl ? (KeyUsageSettingImpl) keyUsageSetting : new KeyUsageSettingImpl(keyUsageSetting) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyUsageSettingImpl that = (KeyUsageSettingImpl) o;
        return keyUsage == that.keyUsage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyUsage);
    }
}
