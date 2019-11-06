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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.stream;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;

/**
 * @since 1.0.0
 */
public interface StreamService extends KapuaService {

    /**
     * @param message
     * @param timeout
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    KapuaResponseMessage<?, ?> publish(KapuaDataMessage message, Long timeout) throws KapuaException;
}
