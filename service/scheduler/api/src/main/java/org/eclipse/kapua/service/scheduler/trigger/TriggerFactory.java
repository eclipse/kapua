/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * Trigger factory service definition.
 * 
 * @since 1.0
 * 
 */
public interface TriggerFactory extends KapuaEntityFactory<Trigger, TriggerCreator, TriggerQuery, TriggerListResult> {

    public TriggerProperty newTriggerProperty(String name, String type, String value);
}
