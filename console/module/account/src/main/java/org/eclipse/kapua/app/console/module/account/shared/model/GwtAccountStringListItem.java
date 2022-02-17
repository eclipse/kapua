/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtStringListItem;

public class GwtAccountStringListItem extends GwtStringListItem {

    private static final long serialVersionUID = 2825662240165195455L;

    public void setHasChildAccount(boolean hasChildAccount) {
        set("hasChildAccount", hasChildAccount);
    }

    public boolean hasChildAccount() {
        return (Boolean) get("hasChildAccount");
    }
}
