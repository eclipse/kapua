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
package org.eclipse.kapua.model.config.metatype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Java class for Tmetadata complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Tmetadata"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OCD" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tocd" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Designate" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tdesignate" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="localization" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlRootElement(name = "MetaData", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "Tmetadata", propOrder = {
        "OCD",
        "designate",
        "any",
        "localization",
        "otherAttributes"
}, factoryClass = MetatypeXmlRegistry.class, factoryMethod = "newKapuaTmetadata")
public interface KapuaTmetadata {

    /**
     * Gets the value of the ocd property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ocd property.
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOCD().add(newItem);
     * </pre>
     */
    @XmlElement(name = "OCD", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
    List<KapuaTocd> getOCD();

    void setOCD(List<KapuaTocd> ocd);

    /**
     * Gets the value of the designate property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the designate property.
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDesignate().add(newItem);
     * </pre>
     */
    @XmlElement(name = "Designate", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
    List<KapuaTdesignate> getDesignate();

    void setDesignate(List<KapuaTdesignate> designate);

    /**
     * Gets the value of the any property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     */
    @XmlAnyElement(lax = true)
    List<Object> getAny();

    void setAny(List<Object> any);

    /**
     * Gets the value of the localization property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "localization")
    String getLocalization();

    /**
     * Sets the value of the localization property.
     *
     * @param value allowed object is {@link String }
     */
    void setLocalization(String value);

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * <p>
     * the map is keyed by the name of the attribute and
     * the value is the string value of the attribute.
     * <p>
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    @XmlAnyAttribute
    Map<QName, String> getOtherAttributes();

    void setOtherAttributes(Map<QName, String> otherAttributes);
}
