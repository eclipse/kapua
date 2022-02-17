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
package org.eclipse.kapua.model.config.metatype;

/**
 * <p>
 * Java class for Tscalar complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="Tscalar"&gt;
 *  &lt;restriction base="string"&gt;
 *      &lt;enumeration value="String"/&gt;
 *      &lt;enumeration value="Long"/&gt;
 *      &lt;enumeration value="Double"/&gt;
 *      &lt;enumeration value="Float"/&gt;
 *      &lt;enumeration value="Integer"/&gt;
 *      &lt;enumeration value="Byte"/&gt;
 *      &lt;enumeration value="Char"/&gt;
 *      &lt;enumeration value="Boolean"/&gt;
 *      &lt;enumeration value="Short"/&gt;
 *      &lt;enumeration value="Password"/&gt;
 *  &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 * @since 1.0
 */
public interface KapuaTscalar {

    /**
     * Gets the value property.
     *
     * @return possible object is {@link String } with restricted values
     */
    String value();
}
