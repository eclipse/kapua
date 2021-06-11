/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua.keystore;

import org.eclipse.kapua.service.device.call.kura.model.keystore.KuraKeystoreSignedCertificate;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.keystore.message.internal.response.KeystoreResponsePayload;
import org.eclipse.kapua.service.device.management.keystore.message.internal.response.KeystoreSignedCertificateResponseMessage;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KeystoreSignedCertificateResponseMessage} to {@link KeystoreSignedCertificateResponseMessage}
 *
 * @since 1.5.0
 */
public class TranslatorAppKeystoreSignedCertificateKuraKapua extends AbstractTranslatorAppKeystoreKuraKapua<KeystoreSignedCertificateResponseMessage> {

    private static final Logger LOG = LoggerFactory.getLogger(TranslatorAppKeystoreSignedCertificateKuraKapua.class);

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public TranslatorAppKeystoreSignedCertificateKuraKapua() {
        super(KeystoreSignedCertificateResponseMessage.class);
    }

    @Override
    protected KeystoreResponsePayload translatePayload(KuraResponsePayload kuraResponsePayload) throws InvalidPayloadException {
        try {
            KeystoreResponsePayload keystoreResponsePayload = super.translatePayload(kuraResponsePayload);

            if (kuraResponsePayload.hasBody()) {
                LoggerFactory.getLogger("Test").info("Device Payload: {}", new String(kuraResponsePayload.getBody()));

                KuraKeystoreSignedCertificate kuraKeystoreSignedCertificate;
                try {
                    kuraKeystoreSignedCertificate = readJsonBodyAs(kuraResponsePayload.getBody(), KuraKeystoreSignedCertificate.class);
                } catch (Exception e) {
                    LOG.warn("KEYS-V1/POST/keystore/entries/csr returned not a JSON body... Trying reading body as a String...");

                    String kuraSignedCertificate = new String(kuraResponsePayload.getBody());
                    kuraSignedCertificate = kuraSignedCertificate.replace("\"", "");
                    kuraSignedCertificate = kuraSignedCertificate.replace("\\n", "\n");

                    kuraKeystoreSignedCertificate = new KuraKeystoreSignedCertificate();
                    kuraKeystoreSignedCertificate.setSignedCertificate(kuraSignedCertificate);

                    LOG.warn("KEYS-V1/POST/keystore/entries/csr returned not a JSON body... Trying reading body as a String... DONE!");
                }

                keystoreResponsePayload.setSignedCertificate(translate(kuraKeystoreSignedCertificate));
            }

            return keystoreResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraResponsePayload);
        }
    }
}
