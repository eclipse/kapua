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
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,
  
  user_id 					BIGINT(21) 	  UNSIGNED NOT NULL,
  credential_type			VARCHAR(64)	  NOT NULL,
  credential_key			VARCHAR(255)  NOT NULL,
  credential_subject		VARCHAR(64)   NOT NULL,
  credential_subject_id		BIGINT(21)	  UNSIGNED NOT NULL,
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  
  
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_credential_scope_id ON atht_credential (scope_id);
CREATE INDEX idx_atht_credential_user_id ON atht_credential (scope_id, user_id);
CREATE INDEX idx_atht_credential_type_credential_key ON atht_credential (credential_type, credential_key);
