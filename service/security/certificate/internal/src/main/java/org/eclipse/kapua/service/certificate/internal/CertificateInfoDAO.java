/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.info.CertificateInfoListResult;
import org.eclipse.kapua.service.certificate.info.internal.CertificateInfoImpl;
import org.eclipse.kapua.service.certificate.info.internal.CertificateInfoListResultImpl;

public class CertificateInfoDAO extends ServiceDAO {

    private CertificateInfoDAO() {
    }

    /**
     * Returns the certificate info list matching the provided query
     *
     * @param em                         the {@link EntityManager}
     * @param certificateInfoQuery
     * @return
     * @throws KapuaException
     */
    public static CertificateInfoListResult query(EntityManager em, KapuaQuery<CertificateInfo> certificateInfoQuery)
            throws KapuaException {
        return ServiceDAO.query(em, CertificateInfo.class, CertificateInfoImpl.class, new CertificateInfoListResultImpl(), certificateInfoQuery);
    }

    /**
     * Returns the certificate info count matching the provided query
     *
     * @param em
     * @param userPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<CertificateInfo> userPermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, CertificateInfo.class, CertificateInfoImpl.class, userPermissionQuery);
    }

}
