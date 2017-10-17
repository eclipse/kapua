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
package org.eclipse.kapua.service.certificate.internal;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import com.google.common.collect.Lists;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateErrorCodes;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateException;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSetting;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class CertificateServiceImpl implements CertificateService {

    private X509Certificate certificate;
    private PrivateKey privateKey;
    public static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final CertificateFactory CERTIFICATE_FACTORY= LOCATOR.getFactory(CertificateFactory.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);
    private static final CertificateDomain CERTIFICATE_DOMAIN = new CertificateDomain();

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(CERTIFICATE_DOMAIN, Actions.write, KapuaId.ANY));
            KapuaCertificateSetting setting = KapuaCertificateSetting.getInstance();

            String privateKeyPath = setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_PRIVATE_KEY, "");
            String certificatePath = setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_CERTIFICATE, "");

            if (privateKeyPath.isEmpty() && certificatePath.isEmpty()) {
                // Fallback to generated
                LOGGER.error("No private key and certificate path specified.\nPlease set authentication.session.jwt.private.key and authentication.session.jwt.certificate system properties.");
                throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR);
            } else {
                File certificateFile = new File(certificatePath);
                File privateKeyFile = new File(privateKeyPath);
                certificate = CertificateUtils.readCertificate(certificateFile);
                privateKey = CertificateUtils.readPrivateKey(privateKeyFile);
            }
        });
    }

    @Override
    public Certificate create(CertificateCreator creator) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Certificate find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaListResult<Certificate> query(KapuaQuery<Certificate> query) throws KapuaException {
        Certificate kapuaCertificate = new CertificateImpl(query.getScopeId());
        kapuaCertificate.setPrivateKey(privateKey);
        kapuaCertificate.setCertificate(certificate);
        CertificateListResult result = CERTIFICATE_FACTORY.newListResult();
        result.addItems(Lists.newArrayList(kapuaCertificate));
        return result;
    }

    @Override
    public long count(KapuaQuery<Certificate> query) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Certificate findByName(String name) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Certificate update(Certificate entity) throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
