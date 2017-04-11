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
package org.eclipse.kapua.service.authentication.shiro;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.CertificateService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class CertificateServiceImpl implements CertificateService {

    private final KeyPair keyPair;
    public static final Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaAuthenticationException {
        KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        File privateKeyFile = new File(setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_PRIVATE_KEY, ""));
        File publicKeyFile = new File(setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_PUBLIC_KEY, ""));
        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
            // Fallback to generated
            logger.warn("Unable to read specified private and/or public key. Using random generated keys.");
            RsaJsonWebKey rsaJsonWebKey = null;
            try {
                rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
                keyPair = new KeyPair(rsaJsonWebKey.getPublicKey(), rsaJsonWebKey.getPrivateKey());
            } catch (JoseException e) {
                throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.JWK_FILE_ERROR);
            }
        } else {
            keyPair = new KeyPair(readPublicKey(publicKeyFile), readPrivateKey(privateKeyFile));
        }
    }

    @Override
    public KeyPair getJwtKeyPair() {
        return keyPair;
    }

    private PrivateKey readPrivateKey(File file) throws KapuaAuthenticationException {
        PrivateKey privateKey = null;
        try {
            String keyFromFile = FileUtils.readFileToString(file)
                    .replaceAll("(\r)?\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] decoded = Base64.getDecoder().decode(keyFromFile);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.JWK_FILE_ERROR);
        }
        return privateKey;
    }

    private PublicKey readPublicKey(File file) throws KapuaAuthenticationException {
        PublicKey publicKey = null;
        try {
            String keyFromFile = FileUtils.readFileToString(file)
                    .replaceAll("(\r)?\n", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            byte[] decoded = Base64.getDecoder().decode(keyFromFile);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            publicKey = kf.generatePublic(spec);
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new KapuaAuthenticationException(KapuaAuthenticationErrorCodes.JWK_FILE_ERROR);
        }
        return publicKey;
    }
}
