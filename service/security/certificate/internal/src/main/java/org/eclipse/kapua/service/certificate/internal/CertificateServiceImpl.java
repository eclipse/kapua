/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.EmptyTocd;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.certificate.CertificateDomains;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateErrorCodes;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateException;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSetting;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSettingKeys;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@KapuaProvider
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final CertificateFactory CERTIFICATE_FACTORY = LOCATOR.getFactory(CertificateFactory.class);

    private String certificate;
    private String privateKey;

    private KapuaTocd emptyTocd;

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaCertificateSetting setting = KapuaCertificateSetting.getInstance();

            String privateKeyPath = setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_PRIVATE_KEY);
            String certificatePath = setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_CERTIFICATE);

            if (Strings.isNullOrEmpty(privateKeyPath) && Strings.isNullOrEmpty(certificatePath)) {
                LOG.error("No private key and certificate path specified.\nPlease set authentication.session.jwt.private.key and authentication.session.jwt.certificate system properties.");
                throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR);
            } else {
                certificate = CertificateUtils.readCertificateAsString(KapuaFileUtils.getAsFile(certificatePath));
                privateKey = CertificateUtils.readPrivateKeyAsString(KapuaFileUtils.getAsFile(privateKeyPath));
            }
        });

        emptyTocd = new EmptyTocd(CertificateService.class.getName(), CertificateService.class.getSimpleName());

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
    public CertificateListResult query(KapuaQuery<Certificate> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(CertificateDomains.CERTIFICATE_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Create the default certificate
        CertificateUsage jwtCertificateUsage = new CertificateUsageImpl("JWT");
        Set<CertificateUsage> certificateUsages = Sets.newHashSet(jwtCertificateUsage);

        KeyUsageSetting keyUsageSetting = new KeyUsageSettingImpl();
        keyUsageSetting.setKeyUsage(KeyUsage.DIGITAL_SIGNATURE);
        keyUsageSetting.setAllowed(true);
        keyUsageSetting.setKapuaAllowed(true);

        KapuaCertificateSetting setting = KapuaCertificateSetting.getInstance();

        Certificate kapuaCertificate = new CertificateImpl(KapuaId.ONE);
        kapuaCertificate.setPrivateKey(privateKey);
        kapuaCertificate.setCertificate(certificate);
        kapuaCertificate.getKeyUsageSettings().add(keyUsageSetting);
        kapuaCertificate.setCertificateUsages(certificateUsages);
        kapuaCertificate.setPassword(setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_PRIVATE_KEY_PASSWORD));

        CertificateListResult result = CERTIFICATE_FACTORY.newListResult();
        result.addItem(kapuaCertificate);

        return result;
    }

    @Override
    public long count(KapuaQuery<Certificate> query) {
        return 1L;
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

    @Override
    public Certificate generate(CertificateGenerator generator) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Certificate> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        return emptyTocd;
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return Collections.emptyMap();
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        throw new UnsupportedOperationException();
    }

}
