/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message.request;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;

public interface KapuaRequestMessageFactory extends KapuaObjectFactory {

    KapuaRequestChannel newRequestChannel();

    KapuaRequestMessage<?, ?> newRequestMessage();

    KapuaRequestPayload newRequestPayload();

    KapuaAppProperties newAppProperties(String value);
}
