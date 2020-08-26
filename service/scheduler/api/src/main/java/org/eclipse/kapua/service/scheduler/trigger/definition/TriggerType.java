/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
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

/**
 * {@link TriggerType} definition.
 * <p>
 * Possible 'nature' of the {@link TriggerDefinition}
 *
 * @since 1.1.0
 */
public enum TriggerType {

    /**
     * An time-based {@link TriggerDefinition}.
     *
     * @since 1.1.0
     */
    TIMER,

    /**
     * An event-based {@link TriggerDefinition}.
     *
     * @since 1.1.0
     */
    EVENT

}
