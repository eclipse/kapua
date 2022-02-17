/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.router;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.camel.Exchange;

@XmlRootElement(name = "endChainEndPoint")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
})
public class EndChainEndPoint implements EndPoint {

    @Override
    @XmlTransient
    public boolean matches(Exchange exchange, Object value, String previous, Map<String, Object> properties) {
        return true;
    }

    @Override
    @XmlTransient
    public String getEndPoint(Exchange exchange, Object value, String previous, Map<String, Object> properties) {
        return null;
    }

    @Override
    public void toLog(StringBuffer buffer, String prefix) {
        buffer.append("End chain");
    }

}
