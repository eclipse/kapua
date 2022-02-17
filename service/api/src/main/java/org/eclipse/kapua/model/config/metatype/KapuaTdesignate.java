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
 * Java class for Tdesignate complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Tdesignate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Object" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tobject"/&gt;
 *         &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="pid" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="factoryPid" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="bundle" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="optional" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="merge" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlRootElement(name = "Designate", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "Tdesignate", propOrder = {
        "object",
        "any",
        "pid",
        "factoryPid",
        "bundle",
        "optional",
        "merge",
        "otherAttributes"
}, factoryClass = MetatypeXmlRegistry.class, factoryMethod = "newKapuaTdesignate")
public interface KapuaTdesignate {

    /**
     * Gets the value of the object property.
     *
     * @return possible object is {@link KapuaTobject }
     */
    @XmlElement(name = "Object", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0", required = true)
    KapuaTobject getObject();

    /**
     * Sets the value of the object property.
     *
     * @param value allowed object is   {@link KapuaTobject }
     */
    void setObject(KapuaTobject value);

    /**
     * Gets the value of the any property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getAny().add(newItem);
     * </pre>
     * <p>
     */
    @XmlAnyElement(lax = true)
    List<Object> getAny();

    void setAny(List<Object> any);

    /**
     * Gets the value of the pid property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "pid")
    String getPid();

    /**
     * Sets the value of the pid property.
     *
     * @param value allowed object is {@link String }
     */
    void setPid(String value);

    /**
     * Gets the value of the factoryPid property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "factoryPid")
    String getFactoryPid();

    /**
     * Sets the value of the factoryPid property.
     *
     * @param value allowed object is {@link String }
     */
    void setFactoryPid(String value);

    /**
     * Gets the value of the bundle property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "bundle")
    String getBundle();

    /**
     * Sets the value of the bundle property.
     *
     * @param value allowed object is {@link String }
     */
    void setBundle(String value);

    /**
     * Gets the value of the optional property.
     *
     * @return possible object is {@link Boolean }
     */
    @XmlAttribute(name = "optional")
    Boolean isOptional();

    /**
     * Sets the value of the optional property.
     *
     * @param value allowed object is {@link Boolean }
     */
    void setOptional(Boolean value);

    /**
     * Gets the value of the merge property.
     *
     * @return possible object is {@link Boolean }
     */
    @XmlAttribute(name = "merge")
    Boolean isMerge();

    /**
     * Sets the value of the merge property.
     *
     * @param value allowed object is {@link Boolean }
     */
    void setMerge(Boolean value);

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
