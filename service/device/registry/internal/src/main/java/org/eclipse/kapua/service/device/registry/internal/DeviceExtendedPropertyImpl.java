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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.service.device.registry.DeviceExtendedProperty;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * {@link DeviceExtendedProperty} implementation.
 *
 * @since 1.5.0
 */
@Embeddable
public class DeviceExtendedPropertyImpl implements DeviceExtendedProperty {

    private static final long serialVersionUID = -2417811252908076430L;

    @Basic
    @Column(name = "group_name", nullable = true, updatable = false)
    private String groupName;

    @Basic
    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Basic
    @Column(name = "value", nullable = true, updatable = false)
    private String value;

    @Basic
    @Column(name = "value_clob", nullable = true, updatable = false)
    private String valueClob;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceExtendedPropertyImpl() {
    }

    /**
     * Constructor.
     *
     * @param groupName The {@link #getGroupName()}.
     * @param name      The {@link #getName()}.
     * @param value     The {@link #getValue()}.
     * @since 1.5.0
     */
    public DeviceExtendedPropertyImpl(String groupName, String name, String value) {
        setGroupName(groupName);
        setName(name);
        setValue(value);
    }

    public DeviceExtendedPropertyImpl(@NotNull DeviceExtendedProperty deviceExtendedProperty) {
        this(deviceExtendedProperty.getGroupName(),
                deviceExtendedProperty.getName(),
                deviceExtendedProperty.getValue());
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return getValueClob() == null ? value : getValueClob();
    }

    @Override
    public void setValue(String value) {
        if (value != null && value.length() > 255) {
            setValueClob(value);
            this.value = value.substring(0, 255);
        } else {
            this.value = value;
        }
    }

    /**
     * Gets the {@link #getValue()} for big values.
     * <p>
     * When setting a value which is longer than 255 chars,
     * the value is store in {@link #valueClob} while in {@link #value}
     * is trimmed at 255 chars for indexing performances.
     *
     * @return The full value if greater than 255, or {@code null}
     * @since 1.5.0
     */
    private String getValueClob() {
        return valueClob;
    }

    /**
     * Sets the {@link #getValue()} for big values.
     *
     * @param valueClob The value greater than 255 chars.
     * @since 1.5.0
     */
    private void setValueClob(String valueClob) {
        this.valueClob = valueClob;
    }

    public static DeviceExtendedPropertyImpl parse(DeviceExtendedProperty deviceExtendedProperty) {
        return deviceExtendedProperty != null ?
                deviceExtendedProperty instanceof DeviceExtendedPropertyImpl ?
                        (DeviceExtendedPropertyImpl) deviceExtendedProperty :
                        new DeviceExtendedPropertyImpl(deviceExtendedProperty)
                : null;
    }
}
