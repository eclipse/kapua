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
package org.eclipse.kapua.message.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "metrics")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XmlAdaptedMetrics {

    private List<XmlAdaptedMetric> adaptedMetrics;

    public XmlAdaptedMetrics() {
        super();
    }

    public XmlAdaptedMetrics(List<XmlAdaptedMetric> adaptedItems) {
        setAdaptedMetrics(adaptedItems);
    }

    @XmlElement(name = "metric")
    public List<XmlAdaptedMetric> getAdaptedMetrics() {
        return adaptedMetrics != null ? adaptedMetrics : new ArrayList<>();
    }

    public void setAdaptedMetrics(List<XmlAdaptedMetric> adaptedMetrics) {
        this.adaptedMetrics = adaptedMetrics;
    }
}
