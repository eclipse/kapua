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
package org.eclipse.kapua.broker.core.plugin;

/**
 * ACL constants
 * 
 * @since 1.0
 */
public class AclConstants {

	public static final String VT_TOPIC_PREFIX_TEMPLATE    = "VirtualTopic.{0}";
    public static final String VT_TOPIC_PREFIX             = "VirtualTopic.";
    
    /**
	 * VM connector
	 */
	public final static String CONNECTOR_NAME_VM                = "vm://kapua";
	public final static String VT_CONSUMER_PREFIX               = "Consumer";
	public final static String ACL_HASH                         = "VirtualTopic.>";
	public final static String ACL_AMQ_ADVISORY                 = "VirtualTopic.ActiveMQ.Advisory.>";
	public final static String ACL_CTRL_ACC_REPLY               = "VirtualTopic.$EDC.{0}.*.*.REPLY.>";
	public final static String ACL_CTRL_ACC_CLI_MQTT_LIFE_CYCLE = "VirtualTopic.$EDC.{0}.{1}.MQTT.>";
	public final static String ACL_CTRL_ACC                     = "VirtualTopic.$EDC.{0}.>";
	public final static String ACL_CTRL_ACC_CLI                 = "VirtualTopic.$EDC.{0}.{1}.>";
	public final static String ACL_DATA_ACC                     = "VirtualTopic.{0}.>";
	public final static String ACL_DATA_ACC_CLI                 = "VirtualTopic.{0}.{1}.>";
	public final static String ACL_CTRL_ACC_NOTIFY              = "VirtualTopic.$EDC.{0}.*.*.NOTIFY.{1}.>";
	
	public final static String[] ACL_VT_DURABLE_PREFIX          = new String[]{
    											"Consumer.{0}:AT_LEAST_ONCE.{1}",
    											"Consumer.{0}:EXACTLY_ONCE.{1}"};

	// full client id, with account prepended
	public final static String MULTI_ACCOUNT_CLIENT_ID          = "{0}:{1}";
	
	public final static String PERMISSION_LOG                   = "{0}/{1}/{2} - {3}";

    public static final int    BROKER_CONNECT_IDX               = 0;
    public static final int    DEVICE_MANAGE_IDX                = 1;
    public static final int    DATA_VIEW_IDX                    = 2;
    public static final int    DATA_MANAGE_IDX                  = 3;

}
