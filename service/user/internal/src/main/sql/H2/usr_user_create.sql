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

CREATE TABLE usr_user (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  name               	    VARCHAR(255)  NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,
  status                 	VARCHAR(64)   NOT NULL DEFAULT 'ENABLED',
  display_name           	VARCHAR(255),
  email                  	VARCHAR(255),
  phone_number           	VARCHAR(64),
  user_type					VARCHAR(64)   NOT NULL DEFAULT 'INTERNAL',
  external_id				VARCHAR(255),
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  

  PRIMARY KEY (id),
  CONSTRAINT usr_uc_name UNIQUE (name)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_user_scope_id ON usr_user (scope_id);
