-- *******************************************************************************
-- Copyright (c) 2017 Eurotech and/or its affiliates and others
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

-- changeset schdl_trigger_properties:1

CREATE TABLE schdl_trigger_properties (
  trigger_id     BIGINT(21) UNSIGNED NOT NULL,

  name           VARCHAR(255)  NOT NULL,
  property_type  VARCHAR(1024) NOT NULL,
  property_value TEXT,

  PRIMARY KEY (trigger_id, name),

  FOREIGN KEY (trigger_id) REFERENCES schdl_trigger (id) ON DELETE CASCADE

) ENGINE = InnoDB DEFAULT CHARSET = utf8;

