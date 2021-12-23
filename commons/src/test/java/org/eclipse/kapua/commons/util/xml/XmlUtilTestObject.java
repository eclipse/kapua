/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test {@link Object} to be used in {@link XmlUtilTest}.
 *
 * @since 1.6.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUtilTestObject {

    @XmlElement(name = "string")
    private String string;

    @XmlElementWrapper(name = "integers")
    @XmlElement(name = "integer")
    private List<Integer> integers;

    public XmlUtilTestObject() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public List<Integer> getIntegers() {
        if (integers == null) {
            integers = new ArrayList<>();
        }

        return integers;
    }

    public void addInteger(Integer integer) {
        getIntegers().add(integer);
    }

    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
    }

    public static XmlUtilTestObject create() {
        XmlUtilTestObject object = new XmlUtilTestObject();

        object.setString("test");
        object.setIntegers(Arrays.asList(1, 2));

        return object;
    }
}
