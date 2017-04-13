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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;

public class GwtBrokerNodeCreator implements Serializable
{
	private static final long serialVersionUID = -3488683047161167451L;

    private long brokerClusterId;
    private long brokerNodeId;
	private String iaasMachineImage;
	private String iaasKeyName;
	private String iaasInstanceType;
	private String iaasAvailabilityZone;
	private int numInstances = 1;
		
	public GwtBrokerNodeCreator() {
		super();
	}

	public GwtBrokerNodeCreator(long brokerClusterId) {
		this.brokerClusterId = brokerClusterId;
		this.brokerNodeId = -1;
		this.iaasKeyName = "";
		this.iaasInstanceType = "";
		this.iaasAvailabilityZone = "";
	}
	
//	// Constructor for reprovisioning
//	public GwtBrokerNodeCreator(BrokerNode node) {
//		this.brokerClusterId = node.getBrokerCluster().getId();
//		this.brokerNodeId = node.getId();
//		this.iaasAvailabilityZone = node.getIaasAvailabilityZone();
//		this.iaasInstanceType = node.getIaasInstanceType();
//		this.iaasKeyName = node.getIaasKeyName();
//		this.iaasMachineImage = node.getIaasMachineImage();
//	}

	public long getBrokerClusterId() {
		return brokerClusterId;
	}

	public void setBrokerClusterId(long brokerClusterId) {
		this.brokerClusterId = brokerClusterId;
	}

	public long getBrokerNodeId() {
		return brokerNodeId;
	}

	public void setBrokerNodeId(long brokerNodeId) {
		this.brokerNodeId = brokerNodeId;
	}

	public long getNumInstances() {
		return numInstances;
	}

	public void setNumInstances(int numInstances) {
		this.numInstances = numInstances;
	}

	public String getIaasMachineImage() {
		return iaasMachineImage;
	}

	public void setIaasMachineImage(String iaasMachineImage) {
		this.iaasMachineImage = iaasMachineImage;
	}

	public String getIaasKeyName() {
		return iaasKeyName;
	}

	public void setIaasKeyName(String iaasKeyName) {
		this.iaasKeyName = iaasKeyName;
	}

	public String getIaasInstanceType() {
		return iaasInstanceType;
	}

	public void setIaasInstanceType(String iaasInstanceType) {
		this.iaasInstanceType = iaasInstanceType;
	}

	public String getIaasAvailabilityZone() {
		return iaasAvailabilityZone;
	}

	public void setIaasAvailabilityZone(String iaasAvailabilityZone) {
		this.iaasAvailabilityZone = iaasAvailabilityZone;
	}
}
