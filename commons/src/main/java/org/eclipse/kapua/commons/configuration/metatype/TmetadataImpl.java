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
package org.eclipse.kapua.commons.configuration.metatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.w3c.dom.Element;

/**
 * <p>
 * Java class for Tmetadata complex type.
 * <p>
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
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
 *
 * @since 1.0
 */
public class TmetadataImpl implements KapuaTmetadata {

    protected List<KapuaTocd> ocd;
    protected List<KapuaTdesignate> designate;
    protected List<Object> any;
    protected String localization;
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the ocd property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ocd property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOCD().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TocdImpl }
     */
    @Override
    public List<KapuaTocd> getOCD() {
        if (ocd == null) {
            ocd = new ArrayList<>();
        }
        return new ArrayList<>(this.ocd);
    }

    public void setOCD(List<KapuaTocd> ocd) {
        this.ocd = ocd;
    }

    /**
     * Gets the value of the designate property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the designate property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDesignate().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TdesignateImpl }
     */
    public List<KapuaTdesignate> getDesignate() {
        if (designate == null) {
            designate = new ArrayList<>();
        }
        return new ArrayList<>(this.designate);
    }

    public void setDesignate(List<KapuaTdesignate> designate) {
        this.designate = designate;
    }

    /**
     * Gets the value of the any property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    public void setAny(List<Object> any) {
        this.any = any;
    }

    /**
     * Gets the value of the localization property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLocalization() {
        return localization;
    }

    /**
     * Sets the value of the localization property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocalization(String value) {
        this.localization = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * <p>
     * <p>
     * the map is keyed by the name of the attribute and
     * the value is the string value of the attribute.
     * <p>
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

}
