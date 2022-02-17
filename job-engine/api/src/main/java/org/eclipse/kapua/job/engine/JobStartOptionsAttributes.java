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
package org.eclipse.kapua.job.engine;

import org.eclipse.kapua.model.KapuaEntityAttributes;

/**
 * {@link JobStartOptions} {@link KapuaEntityAttributes}.
 *
 * @since 1.1.0
 */
public class JobStartOptionsAttributes extends KapuaEntityAttributes {

    public static final String TARGET_ID_SUBLIST = "targetIdSublist";

    public static final String FROM_STEP_INDEX = "fromStepIndex";

    public static final String ENQUEUE = "enqueue";

}
