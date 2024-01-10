/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.config;

import org.eclipse.kapua.model.xml.XmlPropertiesAdapted;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * A container for XmlConfigPropertyAdapted organized into an array.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceXmlConfigPropertiesAdapted extends XmlPropertiesAdapted<ServiceXmlConfigPropertyAdapted.ConfigPropertyType, ServiceXmlConfigPropertyAdapted> {
}
