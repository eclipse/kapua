/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.util.xml.FallbackMappingJAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobXmlRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * Broker {@link JAXBContextProvider} implementation.
 * <p>
 * It relies on the {@link FallbackMappingJAXBContextProvider} implementation.
 *
 * @since 1.0.0
 */
public class BrokerJAXBContextProvider extends FallbackMappingJAXBContextProvider implements JAXBContextProvider {

    @Override
    protected List<Class<?>> getClassesToBound() {
        return Arrays.asList(
                // Kapua Service Configuration
                KapuaTmetadata.class,
                KapuaTocd.class,
                KapuaTad.class,
                KapuaTicon.class,
                TscalarImpl.class,
                KapuaToption.class,
                KapuaTdesignate.class,
                KapuaTobject.class,
                MetatypeXmlRegistry.class,

                // TODO: EXT-CAMEL only for test remove when jobs will be defined in their own container
                // Jobs
                Job.class,
                JobListResult.class,
                JobStartOptions.class,
                JobTargetSublist.class,
                JobXmlRegistry.class,

                // Jobs Exception Info
                CleanJobDataExceptionInfo.class,
                JobAlreadyRunningExceptionInfo.class,
                JobEngineExceptionInfo.class,
                JobInvalidTargetExceptionInfo.class,
                JobMissingStepExceptionInfo.class,
                JobMissingTargetExceptionInfo.class,
                JobNotRunningExceptionInfo.class,
                JobResumingExceptionInfo.class,
                JobRunningExceptionInfo.class,
                JobStartingExceptionInfo.class,
                JobStoppingExceptionInfo.class
        );
    }
}
