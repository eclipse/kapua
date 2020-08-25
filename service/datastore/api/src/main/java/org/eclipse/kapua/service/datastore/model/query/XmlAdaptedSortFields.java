/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "sortFields")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedSortFields {

    private List<XmlAdaptedSortField> adaptedSortFields;

    public XmlAdaptedSortFields() {
        super();
    }

    public XmlAdaptedSortFields(List<XmlAdaptedSortField> adaptedItems) {
        setAdaptedSortFields(adaptedItems);
    }

    @XmlElement(name = "sortField")
    public List<XmlAdaptedSortField> getAdaptedSortFields() {
        return adaptedSortFields != null ? adaptedSortFields : new ArrayList<>();
    }

    public void setAdaptedSortFields(List<XmlAdaptedSortField> adaptedSortFields) {
        this.adaptedSortFields = adaptedSortFields;
    }

}
