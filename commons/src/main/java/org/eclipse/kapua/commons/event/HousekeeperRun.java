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
package org.eclipse.kapua.commons.event;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "SYS_HOUSEKEEPER_RUN")
public class HousekeeperRun implements Serializable {

    private static final long serialVersionUID = 1900974835246471451L;

    private String service;
    private Date lastRunOn;
    private String lastRunBy;
    private Long version;

    @Id
    @Column(name = "service")
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_run_on", nullable = false)
    public Date getLastRunOn() {
        return lastRunOn;
    }

    public void setLastRunOn(Date lastRunOn) {
        this.lastRunOn = lastRunOn;
    }

    @Basic
    @Column(name = "last_run_by", nullable = false)
    public String getLastRunBy() {
        return lastRunBy;
    }

    public void setLastRunBy(String lastRunBy) {
        this.lastRunBy = lastRunBy;
    }

    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}