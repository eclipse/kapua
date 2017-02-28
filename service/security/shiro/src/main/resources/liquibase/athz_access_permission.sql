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

--changeset access_permission:1

CREATE TABLE athz_access_permission (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL, 
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  
  access_info_id			BIGINT(21) 	  UNSIGNED NOT NULL,
  
  domain					VARCHAR(64)	  NOT NULL,
  action					VARCHAR(64),
  target_scope_id			BIGINT(21)	  UNSIGNED,
  group_id             	    BIGINT(21) 	  UNSIGNED,
    
  PRIMARY KEY (id),
--  FOREIGN KEY (access_id) REFERENCES athz_access_info(id) ON DELETE CASCADE
  
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_scopeId_accessId_domain_action_targetScopeId_groupId ON athz_access_permission (scope_id, access_info_id, domain, action, target_scope_id, group_id);

INSERT INTO athz_access_permission
	VALUES
		(1, 1, NOW(), 1, 2, 'broker', 'connect', 1, null); -- kapua-broker assigned of permission: broker:connect:1