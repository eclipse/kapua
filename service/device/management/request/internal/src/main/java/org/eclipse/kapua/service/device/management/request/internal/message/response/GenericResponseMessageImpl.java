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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request.internal.message.response;

import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponseMessageImpl;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseChannel;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponsePayload;

/**
 * {@link GenericResponseMessage} implementation.
 *
 * @since 1.0.0
 */
public class GenericResponseMessageImpl extends KapuaResponseMessageImpl<GenericResponseChannel, GenericResponsePayload>
        implements GenericResponseMessage {

    private static final long serialVersionUID = 4792106537414206216L;
}
