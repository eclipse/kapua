/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.app.web.jaxb;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.EntityNotFoundExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.IllegalArgumentExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.IllegalNullArgumentExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobExecutionEnqueuedExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.SubjectUnauthorizedExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ThrowableInfo;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.service.authentication.AuthenticationXmlRegistry;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreXmlRegistry;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobXmlRegistry;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionXmlRegistry;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepXmlRegistry;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerXmlRegistry;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Provide a customized JAXBContext that makes the concrete implementations
 * known and available for marshalling
 *
 * @since 1.0.0
 */
@Provider
@Produces({MediaType.APPLICATION_JSON})
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

    private JAXBContext jaxbContext;

    public JaxbContextResolver() {
        try {
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

            jaxbContext = JAXBContextFactory.createContext(new Class[]{
                    // REST API exception models
                    ThrowableInfo.class,
                    ExceptionInfo.class,

                    SubjectUnauthorizedExceptionInfo.class,

                    EntityNotFoundExceptionInfo.class,
                    IllegalArgumentExceptionInfo.class,
                    IllegalNullArgumentExceptionInfo.class,

                    // Jobs Exception Info
                    CleanJobDataExceptionInfo.class,
                    JobAlreadyRunningExceptionInfo.class,
                    JobEngineExceptionInfo.class,
                    JobExecutionEnqueuedExceptionInfo.class,
                    JobInvalidTargetExceptionInfo.class,
                    JobMissingStepExceptionInfo.class,
                    JobMissingTargetExceptionInfo.class,
                    JobNotRunningExceptionInfo.class,
                    JobResumingExceptionInfo.class,
                    JobRunningExceptionInfo.class,
                    JobStartingExceptionInfo.class,
                    JobStoppingExceptionInfo.class,

                    // Authentication
                    AuthenticationXmlRegistry.class,
                    AccessToken.class,

                    // Device Management Keystore
                    DeviceKeystores.class,
                    DeviceKeystore.class,
                    DeviceKeystoreCertificate.class,
                    DeviceKeystoreItems.class,
                    DeviceKeystoreItem.class,
                    DeviceKeystoreItemQuery.class,
                    DeviceKeystoreKeypair.class,
                    DeviceKeystoreCSRInfo.class,
                    DeviceKeystoreCSR.class,
                    DeviceKeystoreXmlRegistry.class,

                    // Jobs
                    Job.class,
                    JobQuery.class,
                    JobXmlRegistry.class,

                    JobStep.class,
                    JobStepListResult.class,
                    JobStepQuery.class,
                    JobStepXmlRegistry.class,
                    JobStepProperty.class,

                    JobExecution.class,
                    JobExecutionListResult.class,
                    JobExecutionQuery.class,
                    JobExecutionXmlRegistry.class,

                    JobTarget.class,
                    JobTargetListResult.class,
                    JobTargetQuery.class,
                    JobExecutionXmlRegistry.class,

                    JobTargetSublist.class,

                    DeviceCommandInput.class,
                    DevicePackageDownloadRequest.class,
                    DevicePackageDownloadOptions.class,
                    DevicePackageInstallRequest.class,
                    DevicePackageInstallOptions.class,
                    DevicePackageUninstallRequest.class,
                    DevicePackageUninstallOptions.class,
                    DeviceAssets.class,
                    DeviceConfiguration.class,

                    Trigger.class,
                    TriggerListResult.class,
                    TriggerQuery.class,
                    TriggerXmlRegistry.class
            }, properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JAXBContext getContext(Class<?> type) {
        return jaxbContext;
    }

}
