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
package org.eclipse.kapua.model;

import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * {@link KapuaUpdatableEntityUpdateRequest} definition.
 *
 * @since 2.0.0
 */
public class KapuaUpdatableEntityUpdateRequest {

    @XmlElement(name = "optlock")
    public int optlock;

    /**
     * Introduces only for retrocompatibility to an outdated model
     */
    @XmlTransient
    public Properties entityAttributes;

    /**
     * Introduces only for retrocompatibility to an outdated model
     */
    @XmlTransient
    public Properties entityProperties;
}
