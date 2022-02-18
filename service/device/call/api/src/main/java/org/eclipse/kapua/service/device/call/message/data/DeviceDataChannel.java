/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.data;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;

import java.util.List;

/**
 * {@link DeviceDataChannel} definition.
 *
 * @since 1.0.0
 */
public interface DeviceDataChannel extends DeviceChannel {

    /**
     * Gets the semantic parts.
     *
     * @return The semantic parts.
     * @since 1.2.0
     */
    List<String> getSemanticParts();

    /**
     * Sets the semantic parts.
     *
     * @param semanticParts The semantic parts.
     * @since 1.2.0
     */
    void setSemanticParts(List<String> semanticParts);
}
