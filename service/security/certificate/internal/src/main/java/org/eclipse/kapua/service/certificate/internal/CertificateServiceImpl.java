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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateErrorCodes;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateException;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSetting;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSettingKeys;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class CertificateServiceImpl implements CertificateService {

    private String certificate;
    private String privateKey;
    public static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CertificateFactory CERTIFICATE_FACTORY = LOCATOR.getFactory(CertificateFactory.class);

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaCertificateSetting setting = KapuaCertificateSetting.getInstance();

            String privateKeyPath = setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_PRIVATE_KEY);
            String certificatePath = setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_CERTIFICATE);

            if (Strings.isNullOrEmpty(privateKeyPath) && Strings.isNullOrEmpty(certificatePath)) {
                LOGGER.error("No private key and certificate path specified.\nPlease set authentication.session.jwt.private.key and authentication.session.jwt.certificate system properties.");
                throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR);
            } else {
                certificate = CertificateUtils.readCertificateAsString(KapuaFileUtils.getAsFile(certificatePath));
                privateKey = CertificateUtils.readPrivateKeyAsString(KapuaFileUtils.getAsFile(privateKeyPath));
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
    public CertificateListResult query(KapuaQuery<Certificate> query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Create the default certificate
        CertificateUsage certificateUsage = new CertificateUsageImpl("JWT");

        KeyUsageSetting keyUsageSetting = new KeyUsageSettingImpl();
        keyUsageSetting.setKeyUsage(KeyUsage.DIGITAL_SIGNATURE);
        keyUsageSetting.setAllowed(true);
        keyUsageSetting.setKapuaAllowed(true);

        Certificate kapuaCertificate = new CertificateImpl(query.getScopeId());
        kapuaCertificate.setPrivateKey(privateKey);
        kapuaCertificate.setCertificate(certificate);
        kapuaCertificate.getKeyUsageSettings().add(keyUsageSetting);

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

    @Override
    public Certificate generate(CertificateGenerator generator) throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
