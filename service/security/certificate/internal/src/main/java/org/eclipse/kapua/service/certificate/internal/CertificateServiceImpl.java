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
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.api.Certificate;
import org.eclipse.kapua.service.certificate.api.CertificateCreator;
import org.eclipse.kapua.service.certificate.api.CertificateFactory;
import org.eclipse.kapua.service.certificate.api.CertificateListResult;
import org.eclipse.kapua.service.certificate.api.CertificateService;
import org.eclipse.kapua.service.certificate.api.CertificateUtils;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateErrorCodes;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateException;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSetting;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class
CertificateServiceImpl implements CertificateService {

    private final X509Certificate certificate;
    private final PrivateKey privateKey;
    public static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CertificateFactory FACTORY = LOCATOR.getFactory(CertificateFactory.class);

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaCertificateException {
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
            certificate = readCertificate(certificateFile);
            privateKey = readPrivateKey(privateKeyFile);
        }
    }

    private PrivateKey readPrivateKey(File file) throws KapuaCertificateException {
        PrivateKey privateKey;
        try {
            String keyFromFile = FileUtils.readFileToString(file)
                    .replaceAll("(\r)?\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            privateKey = CertificateUtils.convertStringToPrivateKey(keyFromFile);
        } catch (IOException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.PRIVATE_KEY_ERROR, e);
        }
        return privateKey;
    }

    private X509Certificate readCertificate(File file) throws KapuaCertificateException {
        X509Certificate certificate;
        try {
            String certificateFromFile = FileUtils.readFileToString(file)
                    .replaceAll("(\r)?\n", "")
                    .replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "");
            certificate = CertificateUtils.convertStringToCertificate(certificateFromFile);
        } catch (IOException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR, e);
        }
        return certificate;
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
        try {
            Certificate kapuaCertificate = new CertificateImpl(query.getScopeId());
            kapuaCertificate.setPrivateKey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            kapuaCertificate.setCertificate(Base64.getEncoder().encodeToString(certificate.getEncoded()));
            CertificateListResult result = FACTORY.newListResult();
            result.addItems(Lists.newArrayList(kapuaCertificate));
            return result;
        } catch (CertificateEncodingException e) {
            throw new KapuaCertificateException(KapuaCertificateErrorCodes.CERTIFICATE_ERROR, e);
        }
    }

    @Override
    public long count(KapuaQuery<Certificate> query) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }
}
