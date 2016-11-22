package org.eclipse.kapua.service.authentication.shiro.utils;

import java.security.Key;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;

public class RSAUtil {

    private static RsaJsonWebKey rsaJsonWebKey;

    static {
        try {
            // CertificateFactory fact = CertificateFactory.getInstance("X.509");
            // FileInputStream is = new FileInputStream (args[0]);
            // X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
            // PublicKey key = cer.getPublicKey();

            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Key getPrivateKey() {
        return rsaJsonWebKey.getPrivateKey();
    }

    public static Key getPublicKey() {
        return rsaJsonWebKey.getPublicKey();
    }
}
