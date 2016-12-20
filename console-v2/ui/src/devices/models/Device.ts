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
interface Device {
  acceptEncoding: string;
  applicationIdentifiers: string;
  biosVersion: string;
  clientId: string;
  connectionId: string;
  createdBy: string;
  createdOn: string;
  devoceCredentialsMode: string;
  displayName: string;
  firmwareVersion: string;
  id: string;
  jvmVersion: string;
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
