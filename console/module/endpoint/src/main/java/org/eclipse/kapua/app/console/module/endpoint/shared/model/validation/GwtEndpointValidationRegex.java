/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.endpoint.shared.model.validation;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.client.ui.validator.GwtValidationRegex;

public enum GwtEndpointValidationRegex implements GwtValidationRegex, IsSerializable {

    /**
     * ^[a-zA-Z][a-zA-Z0-9\-\.\+]{0,}$
     */
    URI_SCHEME("^[a-zA-Z][a-zA-Z0-9\\-\\.\\+]{0,}$"),

    /**
     * ^((?![0-9\.]+$)(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\-]*[A-Za-z0-9])|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))$,
     */
    URI_DNS("^((?![0-9\\.]+$)(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))$"),

    /**
     * ^[a-zA-Z][a-zA-Z0-9\-\.\+]{0,}$
     */
    URI_PORT("^[0-9]{1,5}$"),

    URI_ORIGIN("^(https?)://([A-Za-z0-9\\-\\.]+)(:(\\d{1-5}))?$");

    private String regex;

    GwtEndpointValidationRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getRegex() {
        return regex;
    }
}
