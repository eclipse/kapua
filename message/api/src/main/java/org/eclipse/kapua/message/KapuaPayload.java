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
package org.eclipse.kapua.message;

import java.util.Map;

public interface KapuaPayload extends Payload
{
    public Map<String, Object> getProperties();

    public void setProperties(Map<String, Object> metrics);

    public byte[] getBody();

    public void setBody(byte[] body);

    public String toDisplayString();
}
