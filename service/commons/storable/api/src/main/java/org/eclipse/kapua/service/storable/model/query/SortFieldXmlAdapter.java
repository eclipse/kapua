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

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link SortField}s {@link XmlAdapter}.
 *
 * @since 1.0.0
 */
public class SortFieldXmlAdapter extends XmlAdapter<XmlAdaptedSortFields, List<SortField>> {

    @Override
    public XmlAdaptedSortFields marshal(List<SortField> sortFields) {
        XmlAdaptedSortFields xmlAdaptedSortFields = new XmlAdaptedSortFields();
        for (SortField sortField : sortFields) {
            xmlAdaptedSortFields.getAdaptedSortFields().add(new XmlAdaptedSortField(sortField.getSortDirection(), sortField.getField()));
        }
        return xmlAdaptedSortFields;
    }

    @Override
    public List<SortField> unmarshal(XmlAdaptedSortFields xmlAdaptedSortFields) throws Exception {
        List<SortField> sortFields = new ArrayList<>();
        for (XmlAdaptedSortField xmlAdaptedSortField : xmlAdaptedSortFields.getAdaptedSortFields()) {
            sortFields.add(SortField.of(xmlAdaptedSortField.getField(), xmlAdaptedSortField.getDirection()));
        }
        return sortFields;
    }
}
