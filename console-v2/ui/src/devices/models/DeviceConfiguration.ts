/*******************************************************************************
* Copyright (c) 2016, 2017 Eurotech and/or its affiliates                        
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
interface DeviceConfiguration {
  definition: Definition;
  id: string;
  properties: Properties;
}

interface Definition {
  AD: AD[];
  Icon: Icon[];
  description: string;
  id: string;
  name: string;
}

interface Icon {
  resource: string;
  size: number;
}

interface AD {
  Option: Option[];
  cardinality: number;
  description: string;
  id: string;
  name: string;
  required: Boolean;
  type: string;
  inputName: string;
}

interface Option {

}

interface Properties {
  property: Property[];
}

interface Property {
  array: Boolean;
  encrypted: Boolean;
  name: string;
  type: string;
  value: any[];
}