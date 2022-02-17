/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin;

import org.apache.activemq.security.AuthorizationMap;

public class KapuaSecurityAclChecker {

    private AuthorizationMap authMap;
    private boolean hasDataView;
    private boolean hasDataManage;
    private boolean hasDeviceView;
    private boolean hasDeviceManage;

}
