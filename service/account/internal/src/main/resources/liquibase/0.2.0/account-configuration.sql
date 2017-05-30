-- *******************************************************************************
-- Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
--
-- All rights reserved. This program and the accompanying materials
-- are made available under the terms of the Eclipse Public License v1.0
-- which accompanies this distribution, and is available at
-- http://www.eclipse.org/legal/epl-v10.html
--
-- Contributors:
--     Eurotech - initial API and implementation
-- *******************************************************************************

-- liquibase formatted sql

-- changeset account-configuration:1

-- WARNING: to be kept in sync with kapua/commons/src/main/resources/liquibase/configuration.sql
CREATE TABLE IF NOT EXISTS sys_configuration (
  scope_id          		 BIGINT(21) 	  UNSIGNED,
  id                         BIGINT(21) 	  UNSIGNED NOT NULL,
  pid						 VARCHAR(255) 	  NOT NULL,
  configurations			 TEXT,
  created_on                 TIMESTAMP(3) 	  DEFAULT 0,
  created_by                 BIGINT(21) 	  UNSIGNED NOT NULL,
  modified_on                TIMESTAMP(3) 	  NOT NULL,
  modified_by                BIGINT(21) 	  UNSIGNED NOT NULL,
  optlock                    INT UNSIGNED,
  attributes				 TEXT,
  properties                 TEXT,
  PRIMARY KEY  (id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX IF NOT EXISTS idx_configurationScopeId ON sys_configuration (scope_id);

INSERT INTO sys_configuration (
	SCOPE_ID,
    ID,
    PID,
    CONFIGURATIONS,
    CREATED_ON,
    CREATED_BY,
    MODIFIED_ON,
    MODIFIED_BY,
    OPTLOCK,
    ATTRIBUTES,
    PROPERTIES)
VALUES (1,
        1,
        'org.eclipse.kapua.service.account.AccountService',
        CONCAT('#', CURRENT_TIMESTAMP(), CHAR(13), CHAR(10),
               'maxNumberChildEntities=0', CHAR(13), CHAR(10),
               'infiniteChildEntities=true'),
        CURRENT_TIMESTAMP(),
        1,
        CURRENT_TIMESTAMP(),
        1,
        0,
        null,
        null);
