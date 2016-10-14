/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration;

public class KuraPassword
{
    private String password;

    public KuraPassword(String password)
    {
        super();
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return password;
    }
}
