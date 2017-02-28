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

--changeset device:1

CREATE TABLE dvc_device (
  scope_id             	    BIGINT(21) 	    UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	    UNSIGNED NOT NULL,
  
  group_id             	    BIGINT(21) 	    UNSIGNED,
  
  created_on             	TIMESTAMP(3)    NULL,
  created_by             	BIGINT(21)      UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP       NULL,
  modified_by            	BIGINT(21)      UNSIGNED NOT NULL,
  
  client_id                 VARCHAR(255)    NOT NULL,
  connection_id             BIGINT(21) 	    UNSIGNED NULL,
  last_event_id             BIGINT(21) 	    UNSIGNED NULL,
  
  status                 	VARCHAR(64)     NOT NULL DEFAULT 'ENABLED',
  display_name              VARCHAR(255), 
  serial_number             VARCHAR(255),
  model_id                  VARCHAR(255),
  imei                      VARCHAR(24),
  imsi                      VARCHAR(15),
  iccid                     VARCHAR(22),
  bios_version              VARCHAR(255),
  firmware_version          VARCHAR(255),
  os_version                VARCHAR(255),
  jvm_version               VARCHAR(255),
  osgi_framework_version    VARCHAR(255),
  app_framework_version     VARCHAR(255),
  app_identifiers           VARCHAR(1024),
  accept_encoding           VARCHAR(255),
  gps_longitude             DECIMAL(11,8),
  gps_latitude              DECIMAL(11,8),
  custom_attribute_1        VARCHAR(255),
  custom_attribute_2        VARCHAR(255),
  custom_attribute_3        VARCHAR(255),
  custom_attribute_4        VARCHAR(255),
  custom_attribute_5        VARCHAR(255),
  credentials_mode          VARCHAR(64)   NOT NULL,
  preferred_user_id			BIGINT(21)    DEFAULT 0,
  
  optlock                   INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,   
  
  PRIMARY KEY (scope_id, id),   -- primary key needs to include the partitioning key
  CONSTRAINT uc_clientId UNIQUE (scope_id, client_id),
  CONSTRAINT uc_imei UNIQUE (scope_id, imei),
  CONSTRAINT uc_imsi UNIQUE (scope_id, imsi),
  CONSTRAINT uc_iccid UNIQUE (scope_id, iccid)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_device_connection_id ON dvc_device (scope_id, connection_id);
CREATE INDEX idx_device_serial_number ON dvc_device (scope_id, serial_number);
CREATE INDEX idx_device_display_name ON dvc_device (scope_id, display_name);
CREATE INDEX idx_device_status_id ON dvc_device (scope_id, status, client_id);
CREATE INDEX idx_device_status_dn ON dvc_device (scope_id, status, display_name);
CREATE INDEX idx_device_model_id ON dvc_device (scope_id, model_id, client_id);
CREATE INDEX idx_device_model_dn ON dvc_device (scope_id, model_id, display_name);
CREATE INDEX idx_device_esf_id ON dvc_device (scope_id, app_framework_version, client_id);
CREATE INDEX idx_device_esf_dn ON dvc_device (scope_id, app_framework_version, display_name);
CREATE INDEX idx_device_app_id ON dvc_device (scope_id, app_identifiers, client_id);
CREATE INDEX idx_device_app_dn ON dvc_device (scope_id, app_identifiers, display_name);
CREATE INDEX idx_device_c1_id ON dvc_device (scope_id, custom_attribute_1, client_id);
CREATE INDEX idx_device_c1_dn ON dvc_device (scope_id, custom_attribute_1, display_name);
CREATE INDEX idx_device_c2_id ON dvc_device (scope_id, custom_attribute_2, client_id);
CREATE INDEX idx_device_c2_dn ON dvc_device (scope_id, custom_attribute_2, display_name);
CREATE INDEX idx_device_preferred_user_id ON dvc_device (scope_id, preferred_user_id);
