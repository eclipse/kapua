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
package org.eclipse.kapua.app.console.shared.model.account;

import java.io.Serializable;

import org.eclipse.kapua.app.console.shared.model.GwtOrganization;
import org.eclipse.kapua.app.console.shared.model.GwtUpdatableEntityModel;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtAccount extends GwtUpdatableEntityModel implements Serializable {

    private static final long serialVersionUID = -5999185569672904770L;

    //
    // Defines value for service plan
    public static final int ADMIN_ACCOUNT_ID = 1;
    public static final int SERVICE_PLAN_UNLIMITED = -1;
    public static final int SERVICE_PLAN_DISABLED = 0;

    public enum GwtAccountStatus implements IsSerializable {
        BEING_PROVISIONED, ENABLED, DISABLED, BEING_DELETED;

        GwtAccountStatus() {
        };
    }

    public enum GwtAccountProvisioningStatus implements IsSerializable {
        WAITING_TO_START, IN_PROGRESS, FAILED, COMPLETED;

        GwtAccountProvisioningStatus() {
        }
    }

    public enum GwtAccountDataIndexBy implements IsSerializable {
        SERVER_TIMESTAMP, DEVICE_TIMESTAMP;

        GwtAccountDataIndexBy() {
        }
    }

    public enum GwtAccountMetricsIndexBy implements IsSerializable {
        TIMESTAMP, VALUE, NONE;

        GwtAccountMetricsIndexBy() {
        }
    }

    private String brokerUrl;
    private GwtOrganization gwtOrganization;

    public GwtAccount() {
        super();
    }

    public String getName() {
        return (String) get("name");
    }

    public String getUnescapedName() {
        return (String) getUnescaped("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getPassword() {
        return (String) get("password");
    }

    public void setPassword(String password) {
        set("password", password);
    }

    public GwtOrganization getGwtOrganization() {
        return gwtOrganization;
    }

    public void setGwtOrganization(GwtOrganization gwtOrganization) {
        this.gwtOrganization = gwtOrganization;
    }

    public String getBrokerURL() {
        return brokerUrl;
    }

    public void setBrokerURL(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public void setParentAccountId(String parentAccountId) {
        set("parentAccountId", parentAccountId);
    }

    public String getParentAccountId() {
        return (String) get("parentAccountId");
    }

    public void setDashboardPreferredTopic(String dashboardPreferredTopic) {
        set("dashboardPreferredTopic", dashboardPreferredTopic);
    }

    public String getDashboardPreferredTopic() {
        return (String) get("dashboardPreferredTopic");
    }

    public String getUnescapedDashboardPreferredTopic() {
        return (String) getUnescaped("dashboardPreferredTopic");
    }

    public void setDashboardPreferredMetric(String dashboardPreferredMetric) {
        set("dashboardPreferredMetric", dashboardPreferredMetric);
    }

    public String getDashboardPreferredMetric() {
        return (String) get("dashboardPreferredMetric");
    }

    public String getUnescapedDashboardPreferredMetric() {
        return (String) getUnescaped("dashboardPreferredMetric");
    }

    public String toString() {
        return getName();
    }

    public void setOptlock(int optlock) {
        set("optlock", optlock);
    }

    public int getOptlock() {
        return (Integer) get("optlock");
    }

}