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

--changeset commons:1

CREATE TABLE sys_configuration (
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

CREATE INDEX idx_configurationScopeId ON sys_configuration (scope_id);
