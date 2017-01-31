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

CREATE TABLE atht_credential (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,
  
  subject_type				VARCHAR(64)   NOT NULL,
  subject_id				BIGINT(21)	  UNSIGNED,

  type						VARCHAR(64)	  NOT NULL,
  key						VARCHAR(255)  NOT NULL,
  secret				 	VARCHAR(255)  NOT NULL,
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  
  
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_credential_scope_id ON atht_credential (scope_id);
CREATE INDEX idx_atht_credential_subject_type_subject_id ON atht_credential (scope_id, subject_type, subject_id);
CREATE INDEX idx_atht_credential_type_credential_key ON atht_credential (type, key);
