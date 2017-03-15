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
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.CertificateService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;

@KapuaProvider
public class CertificateServiceImpl implements CertificateService {

    private final KeyPair keyPair;
    private static final String PRIVATE_KEY_LOCAL_FILENAME = "jwk_private.key";
    private static final String PUBLIC_KEY_LOCAL_FILENAME = "jwk_public.key";

    /**
     * Constructor
     */
    public CertificateServiceImpl() throws KapuaAuthenticationException {
        KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        File privateKeyFile = new File(setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_JWK_PRIVATE_KEY, ""));
        File publicKeyFile = new File(setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_JWK_PUBLIC_KEY, ""));
        keyPair = new KeyPair(getPublicKey(publicKeyFile), getPrivateKey(privateKeyFile));
    }

    @Override
    public KeyPair getJwtKeyPair() {
        return keyPair;
    }

    private PrivateKey getPrivateKey(File file) throws KapuaAuthenticationException {
        PrivateKey privateKey = null;
        try {
            String keyFromFile = null;
            if (file.exists()) {
                keyFromFile = FileUtils.readFileToString(file);
            } else {
                keyFromFile = ResourceUtils.readResource(ResourceUtils.getResource(PRIVATE_KEY_LOCAL_FILENAME));
            }
            keyFromFile = keyFromFile
                    .replace("-----BEGIN PRIVATE KEY-----\n", "")
                    .replaceAll("\n", "")
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

    private PublicKey getPublicKey(File file) throws KapuaAuthenticationException {
        PublicKey publicKey = null;
        try {
            String keyFromFile = null;
            if (file.exists()) {
                keyFromFile = FileUtils.readFileToString(file);
            } else {
                keyFromFile = ResourceUtils.readResource(ResourceUtils.getResource(PUBLIC_KEY_LOCAL_FILENAME));
            }
            keyFromFile = keyFromFile
                    .replace("-----BEGIN PUBLIC KEY-----\n", "")
                    .replaceAll("\n", "")
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
