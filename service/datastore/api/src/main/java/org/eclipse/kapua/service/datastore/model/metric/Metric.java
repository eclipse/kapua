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
package org.eclipse.kapua.service.datastore.model.metric;

import org.eclipse.kapua.service.datastore.model.MetricInfoTypeAdapter;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link Metric} definition.
 *
 * @param <T> The {@link Class} type.
 * @since 1.0.0
 */
@XmlRootElement(name = "metric")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"name", "type", "value"})
public interface Metric<T> extends Comparable<T> {

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the {@link Class}.
     *
     * @return The {@link Class}.
     * @since 1.0.0
     */
    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(MetricInfoTypeAdapter.class)
    Class<T> getType();

    /**
     * Sets the {@link Class}.
     *
     * @param type The {@link Class}.
     * @since 1.0.0
     */
    void setType(Class<T> type);

    /**
     * Gets the value.
     *
     * @return The value.
     * @since 1.0.0
     */
    @XmlElement(name = "value")
    @XmlPath(".")
    T getValue();

    /**
     * Sets the value.
     *
     * @param value The value.
     * @since 1.0.0
     */
    void setValue(T value);
}
