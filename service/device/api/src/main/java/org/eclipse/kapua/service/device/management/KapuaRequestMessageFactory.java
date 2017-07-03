/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;

public interface KapuaRequestMessageFactory extends KapuaObjectFactory {
    KapuaRequestChannel newRequestChannel();

    KapuaRequestMessage newRequestMessage();

    KapuaRequestPayload newRequestPayload();

    KapuaAppProperties newAppProperties(String value);
}
