/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EmptyTocd implements KapuaTocd {

    private String id;
    private String name;

    public EmptyTocd() {
    }

    public EmptyTocd(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        // No OP implementation
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setIcon(List<? extends KapuaTicon> icon) {
        // No OP implementation
    }

    @Override
    public void setDescription(String value) {
        // No OP implementation
    }

    @Override
    public void setAny(List<Object> any) {
        // No OP implementation
    }

    @Override
    public void setAD(List<? extends KapuaTad> icon) {
        // No OP implementation
    }

    @Override
    public Map<QName, String> getOtherAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
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
