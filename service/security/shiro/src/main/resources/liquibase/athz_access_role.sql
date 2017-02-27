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

--changeset access_role:1

CREATE TABLE athz_access_role (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL, 
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  
  access_info_id			BIGINT(21) 	  UNSIGNED NOT NULL,
  role_id					BIGINT(21) 	  UNSIGNED NOT NULL,
    
  PRIMARY KEY (id),
--  FOREIGN KEY (access_id) REFERENCES athz_access_info(id) ON DELETE CASCADE,
--  FOREIGN KEY (role_id) REFERENCES athz_role(id) ON DELETE RESTRICT
  
) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_scopeId_accessId_roleId ON athz_access_role (scope_id, access_info_id, role_id);

INSERT INTO athz_access_role
	VALUES
		(1, 1, NOW(), 1, 1, 1); -- kapua-sys assigned of role admin