/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
