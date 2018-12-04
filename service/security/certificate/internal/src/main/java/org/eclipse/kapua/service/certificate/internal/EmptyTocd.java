/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.service.certificate.PrivateCertificateService;

public class EmptyTocd implements KapuaTocd {

    private static final EmptyTocd INSTANCE = new EmptyTocd();

    public static EmptyTocd getInstance() {
        return INSTANCE;
    }

    private EmptyTocd() {
    }

    @Override
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
    }

    @Override
    public void setName(String value) {
    }

    @Override
    public void setId(String value) {
    }

    @Override
    public void setIcon(List<? extends KapuaTicon> icon) {
    }

    @Override
    public void setDescription(String value) {
    }

    @Override
    public void setAny(List<Object> any) {
    }

    @Override
    public void setAD(List<? extends KapuaTad> icon) {
    }

    @Override
    public Map<QName, String> getOtherAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public String getName() {
        return PrivateCertificateService.class.getSimpleName();
    }

    @Override
    public String getId() {
        return PrivateCertificateService.class.getName();
    }

    @Override
    public List<KapuaTicon> getIcon() {
        return Collections.emptyList();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<Object> getAny() {
        return Collections.emptyList();
    }

    @Override
    public List<KapuaTad> getAD() {
        return Collections.emptyList();
    }
}
