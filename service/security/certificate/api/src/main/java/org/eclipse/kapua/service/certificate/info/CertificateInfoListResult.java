/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.info;

import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.certificate.info.xml.CertificateInfoXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @since 1.1.0
 */
@XmlRootElement(name = "certificateListResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateInfoXmlRegistry.class, factoryMethod = "newListResult")
public interface CertificateInfoListResult extends KapuaListResult<CertificateInfo> {
}
