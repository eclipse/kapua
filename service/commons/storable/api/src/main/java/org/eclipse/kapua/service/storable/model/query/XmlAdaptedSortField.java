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
package org.eclipse.kapua.service.storable.model.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * {@link SortField} XML adaptation definition.
 *
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sortField")
public class XmlAdaptedSortField {

    @XmlElement
    private String field;

    @XmlElement
    private SortDirection direction;

    /**
     * Constructor.
     * <p>
     * Required by JAXB
     *
     * @since 1.0.0
     */
    public XmlAdaptedSortField() {
    }

    /**
     * Constructor.
     *
     * @param direction The {@link SortDirection}.
     * @param field     The name of the field
     * @since 1.0.0
     */
    public XmlAdaptedSortField(SortDirection direction, String field) {
        this.direction = direction;
        this.field = field;
    }

    /**
     * Gets the field name.
     *
     * @return The field name.
     * @since 1.0.0
     */
    public String getField() {
        return field;
    }

    /**
     * Sets the field name.
     *
     * @param field The field name.
     * @since 1.0.0
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Gets the {@link SortDirection}.
     *
     * @return The {@link SortDirection}.
     * @since 1.0.0
     */
    public SortDirection getDirection() {
        return direction;
    }

    /**
     * Sets the {@link SortDirection}.
     *
     * @param direction The {@link SortDirection}.
     * @since 1.0.0
     */
    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }
}
