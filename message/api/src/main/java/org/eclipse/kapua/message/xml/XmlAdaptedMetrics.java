/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
