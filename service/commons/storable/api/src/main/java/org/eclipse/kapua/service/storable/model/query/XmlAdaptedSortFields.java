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
import java.util.ArrayList;
import java.util.List;

/**
 * {@link List} of {@link SortField} XML adaptation definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "sortFields")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedSortFields {

    private List<XmlAdaptedSortField> adaptedSortFields;

    /**
     * Constructor.
     * <p>
     * Required by JAXB.
     *
     * @since 1.0.0
     */
    public XmlAdaptedSortFields() {
        super();
    }

    /**
     * Gets the {@link List} of {@link SortField}s.
     *
     * @return The {@link List} of {@link SortField}s.
     * @since 1.0.0
     */
    @XmlElement(name = "sortField")
    public List<XmlAdaptedSortField> getAdaptedSortFields() {
        return adaptedSortFields != null ? adaptedSortFields : new ArrayList<>();
    }

    /**
     * Sets the {@link List} of {@link SortField}s.
     *
     * @param adaptedSortFields The {@link List} of {@link SortField}s.
     * @since 1.0.0
     */
    public void setAdaptedSortFields(List<XmlAdaptedSortField> adaptedSortFields) {
        this.adaptedSortFields = adaptedSortFields;
    }

}
