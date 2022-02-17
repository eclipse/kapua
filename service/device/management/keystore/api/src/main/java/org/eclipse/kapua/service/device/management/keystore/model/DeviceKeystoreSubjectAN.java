/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DeviceKeystoreSubjectAN} definition.
 * <p>
 * Identifies the certificate subject alternative names.
 */
@XmlRootElement(name = "subjectAN")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreSubjectAN")
public interface DeviceKeystoreSubjectAN {

    /**
     * The type of the subject alternative name.
     *
     * @return he type of the subject alternative name.
     * @since 1.5.0
     */
    @XmlElement(name = "anType")
    String getANType();

    /**
     * Sets the type of the subject alternative name.
     *
     * @param anType The type of the subject alternative name.
     * @since 1.5.0
     */
    void setANType(String anType);

    /**
     * Gets the alternative name value.
     *
     * @return The alternative name value.
     * @since 1.5.0
     */
    @XmlElement(name = "value")
    String getValue();

    /**
     * Sets the alternative name value.
     *
     * @param value The alternative name value.
     * @since 1.5.0
     */
    void setValue(String value);
}
