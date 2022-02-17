/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message.response;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.message.response.ResponseProperties;

/**
 * Kapua response message payload implementation.
 *
 * @since 1.0
 */
public class KapuaResponsePayloadImpl extends KapuaPayloadImpl implements KapuaResponsePayload {

    @Override
    public String getExceptionMessage() {
        return (String) getMetrics().get(ResponseProperties.RESP_PROPERTY_EXCEPTION_MESSAGE.getValue());
    }

    @Override
    public void setExceptionMessage(String exceptionMessage) {
        getMetrics().put(ResponseProperties.RESP_PROPERTY_EXCEPTION_MESSAGE.getValue(), exceptionMessage);
    }

    @Override
    public String getExceptionStack() {
        return (String) getMetrics().get(ResponseProperties.RESP_PROPERTY_EXCEPTION_STACK.getValue());
    }

    @Override
    public void setExceptionStack(String setExecptionStack) {
        getMetrics().put(ResponseProperties.RESP_PROPERTY_EXCEPTION_STACK.getValue(), setExecptionStack);
    }

}
