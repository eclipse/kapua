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

import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.EmptyTocd;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateAttributes;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateDomains;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.exception.KapuaCertificateException;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSetting;
import org.eclipse.kapua.service.certificate.internal.setting.KapuaCertificateSettingKeys;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class CertificateServiceImpl extends AbstractKapuaService implements CertificateService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final CertificateFactory CERTIFICATE_FACTORY = LOCATOR.getFactory(CertificateFactory.class);

    private final String certificateName = "Default_JWT_Certificate";

    private KapuaTocd emptyTocd;

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaException {
        super(CertificateEntityManagerFactory.getInstance());

        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                CertificateQuery query = CERTIFICATE_FACTORY.newQuery(KapuaId.ONE);
                query.setPredicate(new AttributePredicateImpl<>(CertificateAttributes.NAME, certificateName));
                query.setIncludeInherited(false);

                CertificateListResult result = query(query);

                if (result.isEmpty()) {
                    final String usageName = "JWT";
                    final String description = "Default JWT Certificate";
                    Set<CertificateUsage> certificateUsagesSet = new HashSet<>();
                    certificateUsagesSet.add(CERTIFICATE_FACTORY.newCertificateUsage(usageName));

                    DateTime now = new DateTime();

                    KapuaCertificateSetting setting = KapuaCertificateSetting.getInstance();

                    CertificateGenerator certificateGenerator = CERTIFICATE_FACTORY.newCertificateGenerator();
                    certificateGenerator.setName(certificateName);
                    certificateGenerator.setDescription(description);
                    certificateGenerator.setIssuer(setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_ISSUER));
                    certificateGenerator.setSubject(setting.getString(KapuaCertificateSettingKeys.CERTIFICATE_JWT_ISSUER));
                    certificateGenerator.setNotBefore(now.toDate());
                    certificateGenerator.setNotAfter(now.plusDays(setting.getInt(KapuaCertificateSettingKeys.CERTIFICATE_JWT_VALIDITY)).toDate());
                    certificateGenerator.setKeyLength(setting.getInt(KapuaCertificateSettingKeys.CERTIFICATE_JWT_KEYLENGTH));
                    certificateGenerator.setCertificateUsages(certificateUsagesSet);
                    certificateGenerator.setStatus(CertificateStatus.VALID);
                    certificateGenerator.setForwardable(true);

                    //
                    // Argument validation
                    ArgumentValidator.notNull(certificateGenerator, "certificateGenerator");
                    ArgumentValidator.notNull(certificateGenerator.getName(), "certificateGenerator.name");
                    ArgumentValidator.notNull(certificateGenerator.getDescription(), "certificateGenerator.description");
                    ArgumentValidator.numRange(certificateGenerator.getKeyLength(), 512, Long.MAX_VALUE, "certificateGenerator.keyLength");
                    ArgumentValidator.notNull(certificateGenerator.getIssuer(), "certificateGenerator.issuer");
                    ArgumentValidator.notNull(certificateGenerator.getSubject(), "certificateGenerator.subject");
                    ArgumentValidator.notNull(certificateGenerator.getNotBefore(), "certificateGenerator.notBefore");
                    ArgumentValidator.notNull(certificateGenerator.getNotAfter(), "certificateGenerator.notAfter");
                    ArgumentValidator.notNull(certificateGenerator.getStatus(), "certificateGenerator.status");
                    ArgumentValidator.notNull(certificateGenerator.getForwardable(), "certificateGenerator.forwardable");

                    //
                    // Do generate
                    try {
                        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
                        kpGen.initialize(certificateGenerator.getKeyLength(), new SecureRandom());

                        KeyPair keyPair = kpGen.generateKeyPair();

                        X509Certificate x509Certificate = CertificateUtils.generateCertificate(
                                certificateGenerator.getIssuer(),
                                certificateGenerator.getSubject(),
                                certificateGenerator.getNotBefore(),
                                certificateGenerator.getNotAfter(),
                                keyPair.getPublic(),
                                keyPair.getPrivate()
                        );

                        try (StringWriter certificateSw = new StringWriter(); StringWriter privateKeySw = new StringWriter()) {

                            try (JcaPEMWriter pw = new JcaPEMWriter(certificateSw)) {
                                pw.writeObject(x509Certificate);
                            }

                            try (JcaPEMWriter pw = new JcaPEMWriter(privateKeySw)) {
                                pw.writeObject(new JcaPKCS8Generator(keyPair.getPrivate(), null));
                            }

                            CertificateCreator creator = CERTIFICATE_FACTORY.newCreator(KapuaSecurityUtils.getSession().getScopeId());
                            creator.setName(certificateGenerator.getName());
                            creator.setDescription(certificateGenerator.getDescription());
                            creator.setCertificateUsages(certificateGenerator.getCertificateUsages());
                            creator.setStatus(certificateGenerator.getStatus());
                            creator.setCertificate(certificateSw.toString());
                            creator.setPrivateKey(privateKeySw.toString());
                            creator.setForwardable(certificateGenerator.getForwardable());

                            entityManagerSession.doTransactedAction(em -> CertificateDAO.create(em, creator, x509Certificate));
                        }
                    } catch (Exception t) {
                        throw new KapuaCertificateException(KapuaErrorCodes.INTERNAL_ERROR, t);
                    }
                }
            });
        } catch (Exception e) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, e, "Error creating default certificate");
        }

        emptyTocd = new EmptyTocd(CertificateService.class.getName(), CertificateService.class.getSimpleName());
    }

    @Override
    public Certificate create(CertificateCreator certificateCreator) throws KapuaException {
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

        return entityManagerSession.doAction(em -> CertificateDAO.query(em, query));
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
    public Certificate generate(CertificateGenerator certificateGenerator) throws KapuaException {
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
