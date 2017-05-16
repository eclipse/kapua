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
package org.eclipse.kapua.commons.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Workflow event definition
 * 
 * @since 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface RaiseWorkflowEvents {

    /**
     * Tell when the action should be executed
     *
     */
    enum ACTION_PERFORMED_ON {
        /**
         * Before method call
         */
        BEFORE,
        /**
         * After method call
         */
        AFTER,
        /**
         * After only if the method execution fails
         */
        AFTER_IF_FAIL,
        /**
         * After only if the method execution not fails
         */
        AFTER_IF_SUCCESS
    };

    ACTION_PERFORMED_ON eventActionPerformedOn();
}