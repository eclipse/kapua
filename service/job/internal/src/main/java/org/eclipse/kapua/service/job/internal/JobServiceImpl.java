/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceBase;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.KapuaNamedEntityServiceUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * {@link JobService} implementation
 *
 * @since 1.0.0
 */
@Singleton
public class JobServiceImpl extends KapuaConfigurableServiceBase implements JobService {

    private static final Logger LOG = LoggerFactory.getLogger(JobServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private final JobEngineService jobEngineService = LOCATOR.getService(JobEngineService.class);

    private PermissionFactory permissionFactory;
    private AuthorizationService authorizationService;
    //TODO: make final

    private TriggerService triggerService;
    //TODO: make final
    private TriggerFactory triggerFactory;

    /**
     * Default constructor for injection
     *
     * @param jobEntityManagerFactory The {@link JobEntityManagerFactory} instance
     * @param permissionFactory       The {@link PermissionFactory} instance
     * @param authorizationService    The {@link AuthorizationService} instance
     * @param triggerService          The {@link TriggerService} instance
     * @param triggerFactory          The {@link TriggerFactory} instance
     * @since 2.0.0
     */
    @Inject
    public JobServiceImpl(JobEntityManagerFactory jobEntityManagerFactory,
                          PermissionFactory permissionFactory,
                          AuthorizationService authorizationService,
                          TriggerService triggerService,
                          TriggerFactory triggerFactory,
                          @Named("JobServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager) {
        super(jobEntityManagerFactory, null, serviceConfigurationManager);
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
        this.triggerService = triggerService;
        this.triggerFactory = triggerFactory;
    }

    @Override
    public Job create(JobCreator creator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(creator, "jobCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobCreator.scopeId");
        ArgumentValidator.validateJobName(creator.getName(), "jobCreator.name");

        //
        // Check access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(JobDomains.JOB_DOMAIN, Actions.write, creator.getScopeId()));

        //
        // Check entity limit
        serviceConfigurationManager.checkAllowedEntities(creator.getScopeId(), "Jobs");

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, creator);

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> JobDAO.create(em, creator));
    }

    @Override
    public Job update(Job job) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(job, "job");
        ArgumentValidator.notNull(job.getScopeId(), "job.scopeId");
        ArgumentValidator.validateEntityName(job.getName(), "job.name");

        //
        // Check access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(JobDomains.JOB_DOMAIN, Actions.write, job.getScopeId()));

        //
        // Check existence
        if (find(job.getScopeId(), job.getId()) == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, job.getId());
        }

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, job);

        //
        // Do update
        return entityManagerSession.doTransactedAction(em -> JobDAO.update(em, job));
    }

    @Override
    public Job find(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(JobDomains.JOB_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> JobDAO.find(em, scopeId, jobId));
    }

    @Override
    public JobListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        deleteInternal(scopeId, jobId, false);
    }

    @Override
    public void deleteForced(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        deleteInternal(scopeId, jobId, true);
    }

    //
    // Private methods
    //

    /**
     * Deletes the {@link Job} like {@link #delete(KapuaId, KapuaId)}.
     * <p>
     * If {@code forced} is {@code true} {@link org.eclipse.kapua.service.authorization.permission.Permission} checked will be {@code job:delete:null},
     * and when invoking {@link JobEngineService#cleanJobData(KapuaId, KapuaId)} any exception is logged and ignored.
     *
     * @param scopeId The {@link KapuaId} scopeId of the {@link Job}.
     * @param jobId   The {@link KapuaId} of the {@link Job}.
     * @param forced  Whether or not the {@link Job} must be forcibly deleted.
     * @throws KapuaException In case something bad happens.
     * @since 1.1.0
     */
    private void deleteInternal(KapuaId scopeId, KapuaId jobId, boolean forced) throws KapuaException {

        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(JobDomains.JOB_DOMAIN, Actions.delete, forced ? null : scopeId));

        //
        // Check existence
        if (find(scopeId, jobId) == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Find all the triggers that are associated with this job
        TriggerQuery query = triggerFactory.newQuery(scopeId);
        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_NAME, "jobId"),
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jobId.toCompactId()),
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName())
        );

        query.setPredicate(andPredicate);

        //
        // Query for and delete all the triggers that are associated with this job
        KapuaSecurityUtils.doPrivileged(() -> {
            TriggerListResult triggers = triggerService.query(query);
            for (Trigger trig : triggers.getItems()) {
                triggerService.delete(trig.getScopeId(), trig.getId());
            }
        });

        //
        // Do delete
        try {
            KapuaSecurityUtils.doPrivileged(() -> jobEngineService.cleanJobData(scopeId, jobId));
        } catch (Exception e) {
            if (forced) {
                LOG.warn("Error while cleaning Job data. Ignoring exception since delete is forced! Error: {}", e.getMessage());
                LOG.debug("Error while cleaning Job data. Ignoring exception since delete is forced!", e);
            } else {
                throw e;
            }
        }

        entityManagerSession.doTransactedAction(em -> JobDAO.delete(em, scopeId, jobId));
    }

    /**
     * AuthorizationService should be provided by the Locator, but in most cases when this class is instantiated through the deprecated constructor the Locator is not yet ready,
     * therefore fetching of the required instance is demanded to this artificial getter.
     *
     * @return The instantiated (hopefully) {@link AuthorizationService} instance
     */
    //TODO: Remove as soon as deprecated constructors are removed, use field directly instead.
    protected AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = KapuaLocator.getInstance().getService(AuthorizationService.class);
        }
        return authorizationService;
    }

    /**
     * PermissionFactory should be provided by the Locator, but in most cases when this class is instantiated through this constructor the Locator is not yet ready,
     * therefore fetching of the required instance is demanded to this artificial getter.
     *
     * @return The instantiated (hopefully) {@link PermissionFactory} instance
     */
    //TODO: Remove as soon as deprecated constructors are removed, use field directly instead.
    protected PermissionFactory getPermissionFactory() {
        if (permissionFactory == null) {
            permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);
        }
        return permissionFactory;
    }
}
