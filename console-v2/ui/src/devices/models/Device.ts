/*******************************************************************************
* Copyright (c) 2011, 2016 Eurotech and/or its affiliates                       
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
interface ConnectionModel {
  clientId: string;
  clientIp: string;
  createdBy: string;
  createdOn: string;
  id: string;
  modifiedBy: string;
  modifiedOn: string;
  optlock: number;
  protocol: string;
  scopeId: string;
  status: string;
  userId: any;
}

interface LastEventModel {
  action: string;
  createdBy: string;
  createdOn: string;
  deviceId: any;
  eventMessage: string;
  id: string;
  position: PositionModel;
  receivedOn: string;
  resource: string;
  responseCode: string;
  scopeId: string;
}

interface PositionModel {
  altitude: number; 
  latitude: number; 
  longitude: number; 
}

interface Device {
  acceptEncoding: string;
  applicationIdentifiers: string;
  biosVersion: string;
  clientId: string;
  connection: ConnectionModel;
  connectionId: string;
  createdBy: string;
  createdOn: string;
  deviceCredentialsMode: string;
  displayName: string;
  firmwareVersion: string;
  id: string;
  jvmVersion: string;
  lastEvent: LastEventModel;
  lastEventId: string;
  modelId: string;
  modifiedBy: string;
  modifiedOn: string;
  optlock: number;
  osVersion: string;
  osgiFrameworkVersion: string;
  scopeId: string;
  serialNumber: string;
  status: string;
  type: string;
}
