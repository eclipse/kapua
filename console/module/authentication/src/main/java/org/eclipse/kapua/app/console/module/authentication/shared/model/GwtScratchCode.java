/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authentication.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtScratchCode extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = -469650746033310482L;

    public GwtScratchCode() {
    }

    public GwtScratchCode(String scratchCode) {
        setScratchCode(scratchCode);
    }

    public String getScratchCode() {
        return get("scratchCode");
    }

    public void setScratchCode(String scratchCode) {
        set("scratchCode", scratchCode);
    }

}
