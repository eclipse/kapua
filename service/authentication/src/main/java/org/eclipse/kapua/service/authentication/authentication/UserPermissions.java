/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.authentication;

public class UserPermissions {

    public static final int BROKER_CONNECT_IDX = 0;
    public static final int DEVICE_VIEW_IDX = 1;
    public static final int DEVICE_MANAGE_IDX = 2;
    public static final int DATA_VIEW_IDX = 3;
    public static final int DATA_MANAGE_IDX = 4;

    protected boolean[] hasPermissions;

    public UserPermissions(boolean[] hasPermissions) {
        this.hasPermissions = hasPermissions;
    }

    public boolean hasPermission(int index) {
        return hasPermissions[index];
    }

    public boolean[] hasPermissions() {
        return hasPermissions;
    }

    public boolean isBrokerConnect() {
        return hasPermissions[BROKER_CONNECT_IDX];
    }

    public boolean isDeviceView() {
        return hasPermissions[DEVICE_VIEW_IDX];
    }

    public boolean isDeviceManage() {
        return hasPermissions[DEVICE_MANAGE_IDX];
    }

    public boolean isDataView() {
        return hasPermissions[DATA_VIEW_IDX];
    }

    public boolean isDataManage() {
        return hasPermissions[DATA_MANAGE_IDX];
    }

}
