/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.configuration;

/**
 * Options to customize the ssl behavior of the ES rest client.
 * Available configurations are:
 * <ol>
 * <li> enable ssl without any validation:
 * ssl.enabled=true
 * ssl.trust_server_certificate=false
 * others: unused
 * </li>
 * <li>
 * enable ssl forcing server certificate validation
 * ssl.enabled=true
 * ssl.trust_server_certificate=true
 * keystore_path/ssl.keystore_password empty
 * if ssl.truststore_path/ssl.truststore_password are empty the default jvm truststore will be used (otherwise the provided truststore).
 * </li>
 * <li>enable ssl allowing client certificate validation and forcing server certificate validation
 * ssl.enabled=true
 * ssl.trust_server_certificate=true
 * keystore_path/ssl.keystore_password valued with the proper key store
 * if ssl.truststore_path/ssl.truststore_password are empty the default jvm truststore will be used. Otherwise the provided truststore.
 * </li>
 * </ol>
 *
 * @since 1.3.0
 */
public class ElasticsearchClientSslConfiguration {

    private boolean enabled;
    private boolean trustServiceCertificate;
    private String keyStorePath;
    private String keyStorePassword;
    private String keyStoreType;
    private String trustStorePath;
    private String trustStorePassword;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTrustServiceCertificate() {
        return trustServiceCertificate;
    }

    public void setTrustServiceCertificate(boolean trustServiceCertificate) {
        this.trustServiceCertificate = trustServiceCertificate;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
}
