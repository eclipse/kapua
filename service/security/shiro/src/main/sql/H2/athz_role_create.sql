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

CREATE TABLE athz_role (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on               TIMESTAMP(3)  NOT NULL,
  modified_by               BIGINT(21) 	  UNSIGNED NOT NULL,

  name 						VARCHAR(255)  NOT NULL,
  
  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,
  
  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_role_name ON athz_role (scope_id, name);
