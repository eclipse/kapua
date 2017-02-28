-- *******************************************************************************
-- Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
--
-- All rights reserved. This program and the accompanying materials
-- are made available under the terms of the Eclipse Public License v1.0
-- which accompanies this distribution, and is available at
-- http://www.eclipse.org/legal/epl-v10.html
--
-- Contributors:
--     Eurotech - initial API and implementation
-- *******************************************************************************

--liquibase formatted sql

--changeset credential:1

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
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  
  
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_credential_scope_id ON atht_credential (scope_id);
CREATE INDEX idx_atht_credential_user_id ON atht_credential (scope_id, user_id);
CREATE INDEX idx_atht_credential_type_credential_key ON atht_credential (credential_type, credential_key);

INSERT INTO atht_credential (`scope_id`, `id`, `created_on`, `created_by`, `modified_on`, `modified_by`, `user_id`, `credential_type`, `credential_key`, `optlock`)
		VALUES ('1', '1', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '1', 'PASSWORD', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'), -- Clear text value: kapua-password
			   ('1', '2', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '1', 'API_KEY', '12345678:$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'), -- Clear text value: 12345678kapua-password
			   ('1', '3', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '2', 'PASSWORD', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'); -- Clear text value: kapua-password
