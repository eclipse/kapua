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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons;

@SuppressWarnings("unchecked")
public class DeviceManagementDestinationBuilder<T>
{
    protected String topicPrefix;
    protected String accountName;
    protected String assetId;
    protected String appId;

    public T withTopicPrefix(String topicPrefix)
    {
        this.topicPrefix = topicPrefix;
        return (T) this;
    }

    public T withAccountName(String accountName)
    {
        this.accountName = accountName;
        return (T) this;
    }

    public T withAssetId(String assetId)
    {
        this.assetId = assetId;
        return (T) this;
    }

    public T withAppId(String appId)
    {
        this.appId = appId;
        return (T) this;
    }

    public static class Request extends DeviceManagementDestinationBuilder<Request>
    {
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

        private String              method;
        private String[]            resources;

        public Request withMethod(String method)
        {
            this.method = method;
            return this;
        }

        public Request withResources(String[] resources)
        {
            this.resources = resources;
            return this;
        }

        public String build()
        {
            String resourcesToString = buildResourcesString();

            return String.format(requestTopicStringFormat,
                                 topicPrefix,
                                 accountName,
                                 assetId,
                                 appId,
                                 method,
                                 resourcesToString);

        }

        private String buildResourcesString()
        {
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
