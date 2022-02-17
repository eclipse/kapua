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
package org.eclipse.kapua.app.console.module.device.shared.model.management.keystore;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;

public class GwtDeviceKeystoreCertificate extends GwtEntityModel {

    public String getKeystoreId() {
        return get("keystoreId");
    }

    public void setKeystoreId(String keystoreId) {
        set("keystoreId", keystoreId);
    }

    public String getAlias() {
        return get("alias");
    }

    public void setAlias(String alias) {
        set("alias", alias);
    }

    public String getCertificate() {
        return get("certificate");
    }

    public void setCertificate(String certificate) {
        set("certificate", certificate);
    }
}
