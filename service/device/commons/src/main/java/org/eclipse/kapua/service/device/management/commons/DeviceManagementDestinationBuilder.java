/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons;

/**
 * Device management destination builder.
 * 
 * 
 * @since 1.0
 *
 * @param <T>
 *            destination type
 */
@SuppressWarnings("unchecked")
public class DeviceManagementDestinationBuilder<T> {

    protected String topicPrefix;
    protected String accountName;
    protected String assetId;
    protected String appId;

    /**
     * Add the topic prefix to the destination
     * 
     * @param topicPrefix
     * @return
     */
    public T withTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
        return (T) this;
    }

    /**
     * Add the account name to the destination
     * 
     * @param accountName
     * @return
     */
    public T withAccountName(String accountName) {
        this.accountName = accountName;
        return (T) this;
    }

    /**
     * Add the asset identifier to the destination
     * 
     * @param assetId
     * @return
     */
    public T withAssetId(String assetId) {
        this.assetId = assetId;
        return (T) this;
    }

    /**
     * Add the application identifier to the destination
     * 
     * @param appId
     * @return
     */
    public T withAppId(String appId) {
        this.appId = appId;
        return (T) this;
    }

    /**
     * Provides a destination builder for the device request messages.
     * 
     * @since 1.0
     *
     */
    public static class Request extends DeviceManagementDestinationBuilder<Request> {

        /**
         * <p>
         * Topic format: <br/>
         * {{@link #topicPrefix}}/{{@link #accountName}}/{{@link #assetId}}/{{@link #appId}}/{{@link #method}}[/{@link #resources}]
         * </p>
         * 
         * <p>
         * Topic example: <br/>
         * $KAPUA/kapua-sys/test-asset/CONF-V1/GET/configurations
         * </p>
         * 
         */
        private static final String requestTopicStringFormat = "%s/%s/%s/%s/%s%s";

        private String method;
        private String[] resources;

        /**
         * Add the request method to the destination
         * 
         * @param method
         * @return
         */
        public Request withMethod(String method) {
            this.method = method;
            return this;
        }

        /**
         * Add the resources to the destination
         * 
         * @param resources
         * @return
         */
        public Request withResources(String[] resources) {
            this.resources = resources;
            return this;
        }

        /**
         * Build the destination
         * 
         * @return
         */
        public String build() {
            String resourcesToString = buildResourcesString();

            return String.format(requestTopicStringFormat,
                    topicPrefix,
                    accountName,
                    assetId,
                    appId,
                    method,
                    resourcesToString);

        }

        private String buildResourcesString() {
            StringBuilder sb = new StringBuilder();
            if (resources != null) {
                for (String r : resources) {
                    sb.append("/").append(r);
                }
            }
            return sb.toString();
        }
    }

}
