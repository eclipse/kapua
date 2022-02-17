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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Java class for Tad complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Tad"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Option" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Toption" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="type" use="required" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tscalar" /&gt;
 *       &lt;attribute name="cardinality" type="{http://www.w3.org/2001/XMLSchema}int" default="0" /&gt;
 *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @since 1.0
 */
@XmlRootElement(name = "AD", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "Tad", propOrder = {
        "option",
        "any"
}, factoryClass = MetatypeXmlRegistry.class, factoryMethod = "newKapuaTad")
public interface KapuaTad {

    /**
     * Gets the value of the option property.
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the option property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getOption().add(newItem);
     * </pre>
     * <p>
     */
    @XmlElement(name = "Option", namespace = "http://www.osgi.org/xmlns/metatype/v1.2.0")
    List<KapuaToption> getOption();

    void setOption(List<KapuaToption> option);

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
     * Gets the value of the name property.
     *
     * @return possible object is {@link String}
     */
    @XmlAttribute(name = "name")
    String getName();

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    void setName(String value);

    /**
     * Gets the value of the description property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "description")
    String getDescription();

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is {@link String }
     */
    void setDescription(String value);

    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String}
     */
    @XmlAttribute(name = "id", required = true)
    String getId();

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is {@link String }
     */
    void setId(String value);

    /**
     * Gets the value of the type property.
     *
     * @return possible object is {@link KapuaTscalar }
     */
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(KapuaTscalarAdapter.class)
    KapuaTscalar getType();

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is {@link KapuaTscalar }
     */
    void setType(KapuaTscalar value);

    /**
     * Gets the value of the cardinality property.
     *
     * @return possible object is {@link Integer }
     */
    @XmlAttribute(name = "cardinality")
    Integer getCardinality();

    /**
     * Sets the value of the cardinality property.
     *
     * @param value allowed object is {@link Integer }
     */
    void setCardinality(Integer value);

    /**
     * Gets the value of the min property.
     *
     * @return possible object is  {@link String }
     */
    @XmlAttribute(name = "min")
    String getMin();

    /**
     * Sets the value of the min property.
     *
     * @param value allowed object is {@link String }
     */
    void setMin(String value);

    /**
     * Gets the value of the max property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "max")
    String getMax();

    /**
     * Sets the value of the max property.
     *
     * @param value allowed object is  {@link String }
     */
    void setMax(String value);

    /**
     * Gets the value of the default property.
     *
     * @return possible object is {@link String }
     */
    @XmlAttribute(name = "default")
    String getDefault();

    /**
     * Sets the value of the default property.
     *
     * @param value allowed object is {@link String }
     */
    void setDefault(String value);

    /**
     * Gets the value of the required property.
     *
     * @return possible object is {@link Boolean }
     */
    @XmlAttribute(name = "required")
    Boolean isRequired();

    /**
     * Sets the value of the required property.
     *
     * @param value allowed object is  {@link Boolean }
     */
    void setRequired(Boolean value);

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

}
