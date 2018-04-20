/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.datastore.MetricInfoXmlRegistry;

/**
 * Metric information query result list definition.<br>
 * This object contains the list of the metric information objects retrieved by the search service.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "metricInfos")
@XmlType(factoryClass = MetricInfoXmlRegistry.class, factoryMethod = "newMetricInfoListResult")
public interface MetricInfoListResult extends StorableListResult<MetricInfo> {

}
