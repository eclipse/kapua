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

-- changeset service_events:1

CREATE TABLE IF NOT EXISTS sys_service_event (
  scope_id          		 BIGINT(21) 	  UNSIGNED,
  id                         BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on                 TIMESTAMP(3) 	  DEFAULT 0,
  created_by                 BIGINT(21) 	  UNSIGNED NOT NULL,
  modified_on                TIMESTAMP(3) 	  NOT NULL,
  modified_by                BIGINT(21) 	  UNSIGNED NOT NULL,
  
  context_id                 TEXT,
  event_on	                 TIMESTAMP(3) 	  NOT NULL,
  user_id                    BIGINT(21) 	  UNSIGNED NOT NULL,
  service					VARCHAR(255) NOT NULL,
  entity_type				VARCHAR(255) NOT NULL,
  entity_id                  BIGINT(21) 	  UNSIGNED NOT NULL,
  operation					VARCHAR(64) NOT NULL,
  inputs						TEXT NOT NULL,
  outputs					TEXT,
  status						TEXT DEFAULT 'created',
  note						TEXT,
  
  optlock                   	INT UNSIGNED,
  attributes				    TEXT,
  properties                TEXT,
  
  PRIMARY KEY  (id)
  
) ENGINE = InnoDB DEFAULT CHARSET = utf8;