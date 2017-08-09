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

-- changeset sys-service-event-housekeeping-seed:1

CREATE TABLE IF NOT EXISTS sys_service_event_housekeeping (
  service                    VARCHAR(64),
  last_run_on                TIMESTAMP(3) 	  DEFAULT 0,
  last_run_by                VARCHAR(64),
  version                    BIGINT(21)       UNSIGNED NOT NULL,

  PRIMARY KEY  (service)

) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO sys_service_event_housekeeping (service, last_run_on, version) VALUES ('user', CURRENT_TIMESTAMP(), 1);