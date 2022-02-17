/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Service configuration creator reference implementation.
 *
 * @since 1.0
 *
 */
public class ServiceConfigCreatorImpl extends AbstractKapuaUpdatableEntityCreator<ServiceConfig>
        implements ServiceConfigCreator {

    private static final long serialVersionUID = 7508550960304732465L;

    @XmlElement(name = "pid")
    private String pid;

    @XmlElement(name = "configurations")
    private Properties configurations;

    /**
     * Constructor
     *
     * @param scopeId
     */
    protected ServiceConfigCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getPid() {
        return pid;
    }

    @Override
    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public Properties getConfigurations() {
        return this.configurations;
    }

    @Override
    public void setConfigurations(Properties configurations) {
        this.configurations = configurations;
    }
}
