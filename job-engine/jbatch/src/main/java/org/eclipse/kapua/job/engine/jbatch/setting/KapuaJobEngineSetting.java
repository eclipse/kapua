/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link org.eclipse.kapua.job.engine.jbatch.JobEngineServiceJbatch} setting implementation.
 */
public class KapuaJobEngineSetting extends AbstractKapuaSetting<KapuaJobEngineSettingKeys> {

    private static final String JOB_ENGINE_SETTING_RESOURCE = "kapua-job-engine-setting.properties";

    private static final KapuaJobEngineSetting INSTANCE = new KapuaJobEngineSetting();

    /**
     * Construct a new job engine setting reading settings from {@link KapuaJobEngineSetting#JOB_ENGINE_SETTING_RESOURCE}
     */
    private KapuaJobEngineSetting() {
        super(JOB_ENGINE_SETTING_RESOURCE);
    }

    /**
     * Return the job engine setting instance (singleton)
     *
     * @return
     */
    public static KapuaJobEngineSetting getInstance() {
        return INSTANCE;
    }
}
