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

import org.eclipse.kapua.app.console.shared.model.GwtStringListItem;

public class GwtAccountStringListItem extends GwtStringListItem
{
    private static final long serialVersionUID = 2825662240165195455L;

    public void setHasChildAccount(boolean hasChildAccount)
    {
        set("hasChildAccount", hasChildAccount);
    }

    public boolean hasChildAccount()
    {
        return (Boolean) get("hasChildAccount");
    }
}
