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
package org.eclipse.kapua.service.device.management;

public interface KapuaAppChannel extends KapuaControlChannel
{
    public KapuaAppProperties getAppName();

    public void setAppName(KapuaAppProperties app);

    public KapuaAppProperties getVersion();

    public void setVersion(KapuaAppProperties version);
}
