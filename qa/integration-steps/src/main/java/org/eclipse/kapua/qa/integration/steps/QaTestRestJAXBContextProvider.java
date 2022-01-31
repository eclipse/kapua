/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.integration.steps;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobScopedEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningMultipleResponse;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningResponse;
import org.eclipse.kapua.app.api.core.model.job.MultipleJobIdRequest;
import org.eclipse.kapua.commons.util.xml.DefaultJAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserXmlRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * QA Tests Rest {@link JAXBContextProvider} implementation.
 * <p>
 * It relies {@link DefaultJAXBContextProvider} implementation.
 *
 * @since 1.0.0
 */
public class QaTestRestJAXBContextProvider extends DefaultJAXBContextProvider implements JAXBContextProvider {

    private static final List<Class<?>> CLASSES_TO_BOUND = Arrays.asList(
            // Account
            Account.class,
            AccountCreator.class,
            AccountListResult.class,

            // Authorization
            AccessToken.class,

            // Job Engine
            JobStartOptions.class,

            // Jobs Engine Exception Info
            CleanJobDataExceptionInfo.class,
            JobAlreadyRunningExceptionInfo.class,
            JobEngineExceptionInfo.class,
            JobScopedEngineExceptionInfo.class,
            JobInvalidTargetExceptionInfo.class,
            JobMissingStepExceptionInfo.class,
            JobMissingTargetExceptionInfo.class,
            JobNotRunningExceptionInfo.class,
            JobResumingExceptionInfo.class,
            JobRunningExceptionInfo.class,
            JobStartingExceptionInfo.class,
            JobStoppingExceptionInfo.class,
            IsJobRunningResponse.class,
            IsJobRunningMultipleResponse.class,
            MultipleJobIdRequest.class,

            // User
            User.class,
            UserCreator.class,
            UserListResult.class,
            UserQuery.class,
            UserXmlRegistry.class
    );

    @Override
    protected List<Class<?>> getClassesToBound() {
        return CLASSES_TO_BOUND;
    }
}
