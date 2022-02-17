/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.internal.device.lifecycle.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.HashMap;

/**
 * {@link BirthExtendedProperty} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("deviceExtendedProperty")
public class BirthExtendedProperty extends HashMap<String, String> {

    private static final long serialVersionUID = 650296704022076045L;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public BirthExtendedProperty() {
    }
}
