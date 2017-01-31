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

CREATE TABLE atht_access_token (
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
  
  credential_id				BIGINT(21)	  UNSIGNED NOT NULL,
  token_id					TEXT	      NOT NULL,
  expires_on				TIMESTAMP(3)  NOT NULL,
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  
  
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_access_token_scope_id ON atht_access_token (scope_id);
CREATE INDEX idx_atht_access_token_user_id ON atht_access_token (scope_id, subject_type, subject_id);
