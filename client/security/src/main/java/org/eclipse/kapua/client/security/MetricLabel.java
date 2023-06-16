/*******************************************************************************
 * Copyright (c) 2022, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security;

public class MetricLabel {

    private MetricLabel() {
    }

    public static final String COMPONENT_LOGIN = "login";
    public static final String COMPONENT_PUBLISH = "publish";
    public static final String COMPONENT_SUBSCRIBE = "subscribe";

    public static final String ALLOWED = "allowed";
    public static final String NOT_ALLOWED = "not_allowed";

}
