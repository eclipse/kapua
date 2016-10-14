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

import org.eclipse.kapua.app.console.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.client.util.DateUtils;

import com.google.gwt.core.client.GWT;

public class GwtGroupedNVPair extends KapuaBaseModel implements Serializable
{
    private static final long serialVersionUID = 6017065568183482351L;

    public GwtGroupedNVPair()
    {
    }

    public GwtGroupedNVPair(String group, String name, String value)
    {
        setGroup(group);
        setName(name);
        setValue(value);
    }

    public void setGroup(String group)
    {
        set("group", group);
    }

    public String getGroup()
    {
        return get("group");
    }

    public String getGroupLoc()
    {
        return get("groupLoc");
    }

    public void setId(String id)
    {
        set("id", id);
    }

    public String getId()
    {
        return get("id");
    }

    public void setName(String name)
    {
        set("name", name);
    }

    public String getName()
    {
        return get("name");
    }

    public String getUnescapedName()
    {
        return getUnescaped("name");
    }

    public String getNameLoc()
    {
        return get("nameLoc");
    }

    public void setValue(String value)
    {
        set("value", value);
    }

    public String getValue()
    {
        return get("value");
    }

    public void setStatus(String status)
    {
        set("status", status);
    }

    public String getStatus()
    {
        return get("status");
    }

    public String getStatusLoc()
    {
        return get("statusLoc");
    }

    public void setVersion(String version)
    {
        set("version", version);
    }

    public String getVersion()
    {
        return get("version");
    }

    public String getUnescapedVersion()
    {
        return getUnescaped("version");
    }

    public String getDevLastEventOn()
    {
        return get("devLastEventOn");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X get(String property)
    {
        if ("groupLoc".equals(property)) {
            ValidationMessages msgs = GWT.create(ValidationMessages.class);
            return (X) msgs.getString(getGroup());
        }
        else if ("nameLoc".equals(property)) {
            ValidationMessages msgs = GWT.create(ValidationMessages.class);
            return (X) msgs.getString(getName());
        }
        else if ("statusLoc".equals(property)) {
            ValidationMessages msgs = GWT.create(ValidationMessages.class);
            return (X) msgs.getString(getStatus());
        }
        else if ("devLastEventOn".equals(property)) {
            Date lastEventOn = new Date(Long.valueOf((String) get(property)));
            return (X) DateUtils.formatDateTime(lastEventOn);
        }
        else {
            X value = (X) super.get(property);
            if (value == null) {
                value = (X) "N/A";
            }
            return value;
        }
    }
}
