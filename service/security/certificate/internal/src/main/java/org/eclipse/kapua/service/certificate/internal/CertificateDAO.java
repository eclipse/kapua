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
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateListResult;

import java.security.cert.X509Certificate;

public class CertificateDAO extends ServiceDAO {

    private CertificateDAO() {
    }

    /**
     * Creates and return new {@link Certificate}
     *
     * @param em                 the {@link EntityManager}
     * @param certificateCreator the {@link CertificateCreator}
     * @return the created {@link Certificate}
     * @throws KapuaException the exception
     */
    public static Certificate create(EntityManager em, CertificateCreator certificateCreator, X509Certificate certificate) {
        //
        // Create Certificate
        CertificateImpl certificateImpl = new CertificateImpl(certificateCreator.getScopeId());

        certificateImpl.setName(certificateCreator.getName());
        certificateImpl.setDescription(certificateCreator.getDescription());
        certificateImpl.setCertificate(certificateCreator.getCertificate());
        certificateImpl.setVersion(certificate.getVersion());
        certificateImpl.setSerial(certificate.getSerialNumber().toString());
        certificateImpl.setAlgorithm(certificate.getSigAlgName());
        certificateImpl.setSubject(certificate.getSubjectX500Principal().getName());
        certificateImpl.setIssuer(certificate.getIssuerX500Principal().getName());
        certificateImpl.setNotBefore(certificate.getNotBefore());
        certificateImpl.setNotAfter(certificate.getNotAfter());
        certificateImpl.setStatus(certificateCreator.getStatus());
        certificateImpl.setSignature(certificate.getSignature());
        certificateImpl.setPrivateKey(certificateCreator.getPrivateKey());
        certificateImpl.setCa(certificate.getBasicConstraints() != -1);
        certificateImpl.setCaId(certificateCreator.getCaId());
        certificateImpl.setPassword(certificateCreator.getPassword());
        certificateImpl.setCertificateUsages(certificateCreator.getCertificateUsages());
        certificateImpl.setForwardable(certificateCreator.getForwardable());
        return ServiceDAO.create(em, certificateImpl);
    }

    /**
     * Returns the certificate list matching the provided query
     *
     * @param em                         the {@link EntityManager}
     * @param certificateQuery
     * @return
     * @throws KapuaException
     */
    public static CertificateListResult query(EntityManager em, KapuaQuery<Certificate> certificateQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Certificate.class, CertificateImpl.class, new CertificateListResultImpl(), certificateQuery);
    }

}
