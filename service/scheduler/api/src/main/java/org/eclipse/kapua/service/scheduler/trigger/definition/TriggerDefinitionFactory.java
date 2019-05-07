/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.definition;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link TriggerDefinitionFactory} definition.
 *
 * @see KapuaEntityFactory
 * @since 1.0.0
 */
public interface TriggerDefinitionFactory extends KapuaEntityFactory<TriggerDefinition, TriggerDefinitionCreator, TriggerDefinitionQuery, TriggerDefinitionListResult> {

    /**
     * Instantiates a new {@link TriggerProperty}.
     *
     * @param name  The name to set into the {@link TriggerProperty}.
     * @param type  The type to set into the {@link TriggerProperty}.
     * @param value The value to set into the {@link TriggerProperty}.
     * @return The newly instantiated {@link TriggerProperty}.
     * @since 1.0.0
     */
    TriggerProperty newTriggerProperty(String name, String type, String value);

}
