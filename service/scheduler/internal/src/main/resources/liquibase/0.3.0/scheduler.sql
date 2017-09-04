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

-- changeset account:1

CREATE TABLE schdl_trigger (
  scope_id          		 BIGINT(21) 	  UNSIGNED,
  id                         BIGINT(21) 	  UNSIGNED NOT NULL,
  name                       VARCHAR(255) 	  NOT NULL,
  created_on                 TIMESTAMP(3) 	  DEFAULT 0,
  created_by                 BIGINT(21) 	  UNSIGNED NOT NULL,
  modified_on                TIMESTAMP(3)     NOT NULL,
  modified_by                BIGINT(21) 	  UNSIGNED NOT NULL,
  
  starts_on					TIMESTAMP(3),
  ends_on					TIMESTAMP(3),
  cron_scheduling			VARCHAR(64),
  retry_interval				BIGINT(21) UNSIGNED,
  
  optlock                    INT UNSIGNED,
  attributes				 	TEXT,
  properties                 TEXT,
  
  PRIMARY KEY (id)
  
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE INDEX idx_trigger_scope_id ON schdl_trigger (scope_id);