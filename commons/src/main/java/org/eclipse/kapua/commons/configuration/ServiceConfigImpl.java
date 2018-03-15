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
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "pid", "scopeId", "id", "name", "createdOn", "createdBy", "modifiedOn", "modifiedBy", "optlock" })
@Entity(name = "ServiceConfig")
@Table(name = "sys_configuration")
/**
 * Service configuration reference implementation.
 * 
 * @since 1.0
 * 
 */
public class ServiceConfigImpl extends AbstractKapuaUpdatableEntity implements ServiceConfig {

    private static final long serialVersionUID = 8699765898092343484L;

    @XmlElement(name = "pid")
    @Basic
    @Column(name = "pid")
    private String pid;

    @XmlTransient
    @Basic
    @Column(name = "configurations")
    protected String configurations;

    /**
     * Constructor
     */
    public ServiceConfigImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public ServiceConfigImpl(KapuaId scopeId) {
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
    public Properties getConfigurations()
            throws KapuaException {
        Properties props = new Properties();
        if (configurations != null) {
            try {
                props.load(new StringReader(configurations));
            } catch (IOException e) {
                throw KapuaException.internalError(e);
            }
        }
        return props;
    }

    @Override
    public void setConfigurations(Properties props)
            throws KapuaException {
        if (props != null) {
            try {
                StringWriter writer = new StringWriter();
                props.store(writer, null);
                configurations = writer.toString();
            } catch (IOException e) {
                throw KapuaException.internalError(e);
            }
        }
    }
}
