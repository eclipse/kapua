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
package org.eclipse.kapua.app.console.module.about.shared.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BeanModelTag;

public class GwtAboutDependency implements Serializable, BeanModelTag {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String version;

    private String license;

    private String licenseUrl;

    private String licenseText;

    private String notice;

    private String noticeMimeType;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseText(String licenseText) {
        this.licenseText = licenseText;
    }

    public String getLicenseText() {
        return licenseText;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return notice;
    }

    public void setNoticeMimeType(String noticeMimeType) {
        this.noticeMimeType = noticeMimeType;
    }

    public String getNoticeMimeType() {
        return noticeMimeType;
    }
}
