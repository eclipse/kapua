/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.query.KapuaListResult;

/**
 * Trigger result list definition.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "scheduleListResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TriggerXmlRegistry.class, factoryMethod = "newTriggerListResult")
public interface TriggerListResult extends KapuaListResult<Trigger> {

}
