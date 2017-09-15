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
package org.eclipse.kapua.app.console.module.job.server;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.quartz.CronExpression;

import java.util.ArrayList;
import java.util.List;

public class GwtTriggerServiceImpl extends KapuaRemoteServiceServlet implements GwtTriggerService {

    @Override
    public PagingLoadResult<GwtTrigger> findByJobId(PagingLoadConfig loadConfig, String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtTrigger> gwtTriggerList = new ArrayList<GwtTrigger>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TriggerService triggerService = locator.getService(TriggerService.class);

            // Convert from GWT entity
            GwtTriggerQuery gwtTriggerQuery = new GwtTriggerQuery();
            gwtTriggerQuery.setScopeId(gwtScopeId);
            gwtTriggerQuery.setJobId(gwtJobId);
            TriggerQuery triggerQuery = GwtKapuaJobModelConverter.convertTriggerQuery(gwtTriggerQuery, loadConfig);

            // query
            TriggerListResult triggerListResult = triggerService.query(triggerQuery);

            // If there are results
            if (!triggerListResult.isEmpty()) {
                // count
                if (triggerListResult.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(triggerService.count(triggerQuery)).intValue();
                } else {
                    totalLength = triggerListResult.getSize();
                }

                // Converto to GWT entity
                for (Trigger trigger : triggerListResult.getItems()) {
                    gwtTriggerList.add(KapuaGwtJobModelConverter.convertTrigger(trigger));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtTrigger>(gwtTriggerList, loadConfig.getOffset(), totalLength);
    }

    @Override
    public GwtTrigger create(GwtXSRFToken xsrfToken, GwtTriggerCreator gwtTriggerCreator) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtTrigger gwtTrigger = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TriggerFactory triggerFactory = locator.getFactory(TriggerFactory.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtTriggerCreator.getScopeId());
            TriggerCreator triggerCreator = triggerFactory.newCreator(scopeId);
            triggerCreator.setName(gwtTriggerCreator.getTriggerName());
            triggerCreator.setStartsOn(gwtTriggerCreator.getStartsOn());
            triggerCreator.setEndsOn(gwtTriggerCreator.getEndsOn());
            triggerCreator.setCronScheduling(gwtTriggerCreator.getCronScheduling());
            triggerCreator.setRetryInterval(gwtTriggerCreator.getRetryInterval());
            triggerCreator.setTriggerProperties(GwtKapuaJobModelConverter.convertTriggerProperties(gwtTriggerCreator.getTriggerProperties()));

            //
            // Create the User
            TriggerService triggerService = locator.getService(TriggerService.class);
            Trigger trigger = triggerService.create(triggerCreator);

            // convert to GwtAccount and return
            // reload the user as we want to load all its permissions
            gwtTrigger = KapuaGwtJobModelConverter.convertTrigger(trigger);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtTrigger;
    }

    @Override
    public GwtTrigger update(GwtXSRFToken xsrfToken, GwtTrigger gwtTrigger) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        GwtTrigger gwtTriggerUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TriggerService triggerService = locator.getService(TriggerService.class);

            KapuaId scopeId = KapuaEid.parseCompactId(gwtTrigger.getScopeId());
            KapuaId triggerId = KapuaEid.parseCompactId(gwtTrigger.getId());

            Trigger trigger = triggerService.find(scopeId, triggerId);

            if (trigger != null) {

                //
                // Update trigger
                trigger.setName(gwtTrigger.getTriggerName());
                trigger.setStartsOn(gwtTrigger.getStartsOn());
                trigger.setEndsOn(gwtTrigger.getEndsOn());
                trigger.setRetryInterval(gwtTrigger.getRetryInterval());
                trigger.setCronScheduling(gwtTrigger.getCronScheduling());

                // optlock
                trigger.setOptlock(gwtTrigger.getOptlock());

                // update the trigger
                gwtTriggerUpdated = KapuaGwtJobModelConverter.convertTrigger(triggerService.update(trigger));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtTriggerUpdated;
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtTriggerId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId triggerId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtTriggerId);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            TriggerService triggerService = locator.getService(TriggerService.class);
            triggerService.delete(scopeId, triggerId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public boolean validateCronExpression(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    @Override
    public GwtTriggerProperty trickGwt() {
        return null;
    }
}
