/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfo;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfoCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtSubjectType;
import org.eclipse.kapua.app.console.shared.service.GwtAccessInfoService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.model.subject.SubjectFactory;
import org.eclipse.kapua.model.subject.SubjectType;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;

public class GwtAccessInfoServiceImpl extends KapuaRemoteServiceServlet implements GwtAccessInfoService {

    private static final long serialVersionUID = 3606053200278262228L;

    @Override
    public GwtAccessInfo create(GwtXSRFToken xsrfToken, GwtAccessInfoCreator gwtAccessInfoCreator) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtAccessInfo gwtAccessInfo = null;
        try {
            // Convert from GWT Entity
            AccessInfoCreator accessInfoCreator = GwtKapuaModelConverter.convert(gwtAccessInfoCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);

            // Convert
            gwtAccessInfo = KapuaGwtModelConverter.convert(accessInfo);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtAccessInfo;
    }

    @Override
    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String accessInfoShortId) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do delete
        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId accessInfoId = GwtKapuaModelConverter.convert(accessInfoShortId);

            // Delete
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            accessInfoService.delete(scopeId, accessInfoId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtAccessInfo findBySubjectOrCreate(String scopeShortId, GwtSubjectType gwtSubjectType, String subjectShortId) throws GwtKapuaException {
        GwtAccessInfo gwtAccessInfo = null;

        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            SubjectType subjectType = GwtKapuaModelConverter.convert(gwtSubjectType);
            KapuaId subjectId = GwtKapuaModelConverter.convert(subjectShortId);

            // Find
            KapuaLocator locator = KapuaLocator.getInstance();
            SubjectFactory subjectFactory = locator.getFactory(SubjectFactory.class);
            Subject subject = subjectFactory.newSubject(subjectType, subjectId);

            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            AccessInfo accessInfo = accessInfoService.findBySubject(subject);

            // Create if not exists
            if (accessInfo == null) {
                AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
                AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(scopeId);
                accessInfoCreator.setSubjectType(subjectType);
                accessInfoCreator.setSubjectId(subjectId);

                accessInfo = accessInfoService.create(accessInfoCreator);
            }
            gwtAccessInfo = KapuaGwtModelConverter.convert(accessInfo);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccessInfo;
    }
}