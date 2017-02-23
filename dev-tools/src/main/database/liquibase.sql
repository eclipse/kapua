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

--changeset hekonsek:1

CREATE TABLE act_account (
  scope_id          		 BIGINT(21) 	  UNSIGNED,
  id                         BIGINT(21) 	  UNSIGNED NOT NULL,
  name                       VARCHAR(255) 	  NOT NULL,
  created_on                 TIMESTAMP(3) 	  DEFAULT 0,
  created_by                 BIGINT(21) 	  UNSIGNED NOT NULL,
  modified_on                TIMESTAMP(3)     NOT NULL,
  modified_by                BIGINT(21) 	  UNSIGNED NOT NULL,
  org_name                   VARCHAR(255) 	  NOT NULL,
  org_person_name            VARCHAR(255) 	  DEFAULT '',
  org_email                  VARCHAR(255) 	  NOT NULL,
  org_phone_number           VARCHAR(64),
  org_address_line_1         VARCHAR(255),
  org_address_line_2         VARCHAR(255),
  org_address_line_3         VARCHAR(255),
  org_zip_postcode           VARCHAR(255),
  org_city                   VARCHAR(255),
  org_state_province_county  VARCHAR(255),
  org_country                VARCHAR(255),
  parent_account_path        VARCHAR(64),
  optlock                    INT UNSIGNED,
  attributes				 TEXT,
  properties                 TEXT,

  PRIMARY KEY (id),
  FOREIGN KEY (scope_id) REFERENCES act_account(id) ON DELETE RESTRICT,
  CONSTRAINT act_accountName UNIQUE (name)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_account_scope_id ON act_account (scope_id);

--changeset hekonsek:2

INSERT INTO `act_account` (
	`scope_id`,
	`id`,
	`name`,
	`created_on`,
	`created_by`,
	`modified_on`,
	`modified_by`,
	`org_name`,
	`org_person_name`,
	`org_email`,
	`org_phone_number`,
	`org_address_line_1`,
	`org_address_line_2`,
	`org_address_line_3`,
	`org_zip_postcode`,
	`org_city`,
	`org_state_province_county`,
	`org_country`,
	`parent_account_path`,
	`optlock`,
	`attributes`,
	`properties`)
VALUES (NULL,
		1,
		'kapua-sys',
		CURRENT_TIMESTAMP(),
		1,
		CURRENT_TIMESTAMP(),
		1,
		'kapua-org',
		'Kapua Sysadmin',
		'kapua-sys@eclipse.org',
		'+1 555 123 4567',
		NULL,
		NULL,
		NULL,
		NULL,
		NULL,
		NULL,
        NULL,
		'\1',
		0,
		NULL,
		NULL);

--changeset hekonsek:3

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

--changeset hekonsek:4

INSERT INTO atht_credential (`scope_id`, `id`, `created_on`, `created_by`, `modified_on`, `modified_by`, `user_id`, `credential_type`, `credential_key`, `optlock`)
		VALUES ('1', '1', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '1', 'PASSWORD', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0'),
			   ('1', '2', CURRENT_TIMESTAMP(), '1', CURRENT_TIMESTAMP(), '1', '2', 'PASSWORD', '$2a$12$BjLeC/gqcnEyk.XNo2qorul.a/v4HDuOUlfmojdSZXRSFTjymPdVm', '0');

--changeset hekonsek:5

CREATE TABLE athz_role (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on               TIMESTAMP(3)  NOT NULL,
  modified_by               BIGINT(21) 	  UNSIGNED NOT NULL,

  name 						VARCHAR(255)  NOT NULL,

  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,

  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_role_name ON athz_role (scope_id, name);

--changeset hekonsek:6

CREATE TABLE athz_role_permission (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,

  role_id             	    BIGINT(21) 	  UNSIGNED,
  domain					VARCHAR(64)   NOT NULL,
  action					VARCHAR(64),
  target_scope_id		    BIGINT(21)    UNSIGNED,
  group_id             	    BIGINT(21) 	  UNSIGNED,

  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_role_permission_scope_id ON athz_role_permission (role_id, domain, action, target_scope_id);

--changeset hekonsek:7

CREATE TABLE athz_user_permission (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  DEFAULT 0,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,

  user_id					BIGINT(21) 	  UNSIGNED NOT NULL,
  domain					VARCHAR(64)   NOT NULL,
  action					VARCHAR(64),
  target_scope_id		    BIGINT(21),

  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_user_permission_scope_id ON athz_user_permission (scope_id, user_id, domain, action, target_scope_id);

--changeset hekonsek:8

INSERT INTO athz_user_permission (`scope_id`, `id`, `created_on`, `created_by`, `user_id`, `domain`)
		VALUES 	('1', '1', CURRENT_TIMESTAMP(), '1', '1', 'account'),
			   	('1', '2', CURRENT_TIMESTAMP(), '1', '1', 'user'),
				('1', '3', CURRENT_TIMESTAMP(), '1', '1', 'device_event'),
				('1', '4', CURRENT_TIMESTAMP(), '1', '1', 'device_connection'),
				('1', '5', CURRENT_TIMESTAMP(), '1', '1', 'device'),
				('1', '6', CURRENT_TIMESTAMP(), '1', '1', 'data'),
				('1', '7', CURRENT_TIMESTAMP(), '1', '1', 'broker'),
				('1', '8', CURRENT_TIMESTAMP(), '1', '1', 'credential'),
				('1', '9', CURRENT_TIMESTAMP(), '1', '1', 'role'),
				('1', '10', CURRENT_TIMESTAMP(), '1', '1', 'user_permission'),
				('1', '11', CURRENT_TIMESTAMP(), '1', '1', 'device_lifecycle'),
				('1', '12', CURRENT_TIMESTAMP(), '1', '1', 'device_management'),
				('1', '101', CURRENT_TIMESTAMP(), '1', '2', 'account'),
				('1', '102', CURRENT_TIMESTAMP(), '1', '2', 'user'),
				('1', '103', CURRENT_TIMESTAMP(), '1', '2', 'device_event'),
				('1', '104', CURRENT_TIMESTAMP(), '1', '2', 'device_connection'),
				('1', '105', CURRENT_TIMESTAMP(), '1', '2', 'device'),
				('1', '106', CURRENT_TIMESTAMP(), '1', '2', 'data'),
				('1', '107', CURRENT_TIMESTAMP(), '1', '2', 'broker'),
				('1', '108', CURRENT_TIMESTAMP(), '1', '2', 'credential'),
				('1', '109', CURRENT_TIMESTAMP(), '1', '2', 'role'),
				('1', '110', CURRENT_TIMESTAMP(), '1', '2', 'user_permission'),
				('1', '111', CURRENT_TIMESTAMP(), '1', '2', 'device_lifecycle'),
				('1', '112', CURRENT_TIMESTAMP(), '1', '2', 'device_management');

--changeset hekonsek:9

CREATE TABLE athz_user_role (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  DEFAULT 0,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,

  user_id					BIGINT(21) 	  UNSIGNED NOT NULL,

  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE INDEX idx_user_role_scope_id ON athz_user_role (scope_id, user_id);

--changeset hekonsek:10

CREATE TABLE athz_user_role_roles (
  user_id					BIGINT(21) 	  UNSIGNED NOT NULL,
  role_id					BIGINT(21) 	  UNSIGNED NOT NULL,

  PRIMARY KEY (user_id, role_id)

) DEFAULT CHARSET=utf8;

--changeset hekonsek:11

CREATE TABLE dvc_device_connection (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by            	BIGINT(21)    UNSIGNED NOT NULL,
  connection_status		    VARCHAR(20)   NOT NULL,
  client_id					VARCHAR(255)  NOT NULL,
  user_id        			BIGINT(21)    UNSIGNED NOT NULL,
  protocol       			VARCHAR(64),
  client_ip      			VARCHAR(255),
  server_ip      			VARCHAR(255),
  optlock                   INT UNSIGNED,
  attributes				 TEXT,
  properties                 TEXT,

  PRIMARY KEY (scope_id, id)   -- primary key needs to include the partitioning key
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_connection_status_id ON dvc_device_connection (scope_id, id, connection_status);

--changeset hekonsek:12

CREATE TABLE dvc_device (
  scope_id             	    BIGINT(21) 	    UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	    UNSIGNED NOT NULL,
  group_id             	    BIGINT(21) 	  UNSIGNED,
  client_id                 VARCHAR(255)    NOT NULL,
  connection_id             BIGINT(21) 	    UNSIGNED NULL,
  created_on             	TIMESTAMP(3)    NULL,
  created_by             	BIGINT(21)      UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP       NULL,
  modified_by            	BIGINT(21)      UNSIGNED NOT NULL,
  status                 	VARCHAR(64)     NOT NULL DEFAULT 'ENABLED',
  display_name              VARCHAR(255),
  last_event_on             TIMESTAMP(3)    NULL DEFAULT NULL,
  last_event_type           VARCHAR(255),
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
  credentials_mode          VARCHAR(64)   NOT NULL DEFAULT 'INHERITED',
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
CREATE INDEX idx_device_status_le ON dvc_device (scope_id, status, last_event_on);
CREATE INDEX idx_device_model_id ON dvc_device (scope_id, model_id, client_id);
CREATE INDEX idx_device_model_dn ON dvc_device (scope_id, model_id, display_name);
CREATE INDEX idx_device_model_le ON dvc_device (scope_id, model_id, last_event_on);
CREATE INDEX idx_device_esf_id ON dvc_device (scope_id, app_framework_version, client_id);
CREATE INDEX idx_device_esf_dn ON dvc_device (scope_id, app_framework_version, display_name);
CREATE INDEX idx_device_esf_le ON dvc_device (scope_id, app_framework_version, last_event_on);
CREATE INDEX idx_device_app_id ON dvc_device (scope_id, app_identifiers, client_id);
CREATE INDEX idx_device_app_dn ON dvc_device (scope_id, app_identifiers, display_name);
CREATE INDEX idx_device_app_le ON dvc_device (scope_id, app_identifiers, last_event_on);
CREATE INDEX idx_device_c1_id ON dvc_device (scope_id, custom_attribute_1, client_id);
CREATE INDEX idx_device_c1_dn ON dvc_device (scope_id, custom_attribute_1, display_name);
CREATE INDEX idx_device_c1_le ON dvc_device (scope_id, custom_attribute_1, last_event_on);
CREATE INDEX idx_device_c2_id ON dvc_device (scope_id, custom_attribute_2, client_id);
CREATE INDEX idx_device_c2_dn ON dvc_device (scope_id, custom_attribute_2, display_name);
CREATE INDEX idx_device_c2_le ON dvc_device (scope_id, custom_attribute_2, last_event_on);
CREATE INDEX idx_device_preferred_user_id ON dvc_device (scope_id, preferred_user_id);

--changeset hekonsek:13

CREATE TABLE dvc_device_event (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL DEFAULT 0,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,

  device_id					BIGINT(21) 	  UNSIGNED NOT NULL,
  received_on				TIMESTAMP(3)  NOT NULL DEFAULT 0,
  sent_on					TIMESTAMP(3)  NULL DEFAULT NULL,

  pos_longitude				DECIMAL(11,8),
  pos_latitude 	            DECIMAL(11,8),
  pos_altitude              DECIMAL(11,8),
  pos_precision 			DECIMAL(11,8),
  pos_heading				DECIMAL(11,8),
  pos_speed                 DECIMAL(11,8),
  pos_timestamp             TIMESTAMP(3)  NULL DEFAULT 0,
  pos_satellites			INT,
  pos_status				INT,

  resource					VARCHAR(255)  NOT NULL,
  action					VARCHAR(255)  NOT NULL,
  response_code				VARCHAR(255)  NOT NULL,
  event_message				TEXT,

  attributes				 TEXT,
  properties                 TEXT,

  PRIMARY KEY (scope_id, id)   -- primary key needs to include the partitioning key
) CHARSET=utf8;

CREATE INDEX idx_device_event_id ON dvc_device_event (scope_id, device_id, resource, action);

--changeset hekonsek:14

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

--changeset hekonsek:15

CREATE TABLE collision_entity_test (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  name               	    VARCHAR(255)  NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,
  optlock                   INT UNSIGNED,
  test_field             	VARCHAR(255) NOT NULL UNIQUE,
  attributes				TEXT,
  properties                TEXT,

  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_collision_entity_test_scope_id ON collision_entity_test (scope_id);

--changeset hekonsek:16

CREATE TABLE usr_user (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  name               	    VARCHAR(255)  NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,
  status                 	VARCHAR(64)   NOT NULL DEFAULT 'ENABLED',
  display_name           	VARCHAR(255),
  email                  	VARCHAR(255),
  phone_number           	VARCHAR(64),
  optlock               	INT UNSIGNED,
  attributes             	TEXT,
  properties             	TEXT,
  user_type               VARCHAR(64)   NOT NULL DEFAULT 'INTERNAL',
  external_id             VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT usr_uc_name UNIQUE (name)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_user_scope_id ON usr_user (scope_id);

--changeset hekonsek:17

INSERT INTO `usr_user` (`scope_id`, `id`, `name`, `created_on`, `created_by`, `modified_on`, `modified_by`, `status`, `display_name`, `email`, `phone_number`, `optlock`, `attributes`, `properties`)
		VALUES (1, 1, 'kapua-sys',    CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 'ENABLED', 'Kapua Sysadmin', 'kapua-sys@eclipse.org',    '+1 555 123 4567', 0, NULL, NULL),
		       (1, 2, 'kapua-broker', CURRENT_TIMESTAMP(), 1, CURRENT_TIMESTAMP(), 1, 'ENABLED', 'Kapua Broker',   'kapua-broker@eclipse.org', '+1 555 123 4567', 0, NULL, NULL);

--changeset hekonsek:18

CREATE TABLE athz_access_info (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on               TIMESTAMP(3)  NOT NULL,
  modified_by               BIGINT(21) 	  UNSIGNED NOT NULL,

  user_id					BIGINT(21) 	  UNSIGNED NOT NULL,

  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,

  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE INDEX idx_scopeId_userId ON athz_access_info (scope_id, user_id);

--changeset hekonsek:19

INSERT INTO athz_access_info
	VALUES
		(1, 1, NOW(), 1, NOW(), 1, 1, 0, '', ''),
		(1, 2, NOW(), 1, NOW(), 1, 2, 0, '', '');

--changeset hekonsek:20

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

CREATE INDEX idx_scopeId_accessId_domain_action_targetScopeId ON athz_access_permission (scope_id, access_info_id, domain, action, target_scope_id);

--changeset hekonsek:21

INSERT INTO athz_access_permission
	VALUES
		(1, 1, NOW(), 1, 2, 'broker', 'connect', 1, null); -- kapua-broker assigned of permission: broker:connect:1

--changeset hekonsek:22

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

CREATE INDEX idx_scopeId_accessId_roleId ON athz_access_role (scope_id, access_info_id, role_id);

--changeset hekonsek:23

INSERT INTO athz_access_role
	VALUES
		(1, 1, NOW(), 1, 1, 1); -- kapua-sys assigned of role admin

--changeset hekonsek:24

INSERT INTO athz_role
	VALUES
		(1, 1, NOW(), 1, NOW(), 1, 'admin', 0, '','');

--changeset hekonsek:25

INSERT INTO athz_role_permission
	VALUES
		(1, 1, NOW(), 1, 1, 'account', null, null, null),
		(1, 2, NOW(), 1, 1, 'user', null, null, null),
		(1, 3, NOW(), 1, 1, 'device_event', null, null, null),
		(1, 4, NOW(), 1, 1, 'device_connection', null, null, null),
		(1, 5, NOW(), 1, 1, 'device', null, null, null),
		(1, 6, NOW(), 1, 1, 'data', null, null, null),
		(1, 7, NOW(), 1, 1, 'broker', null, null, null),
		(1, 8, NOW(), 1, 1, 'credential', null, null, null),
		(1, 9, NOW(), 1, 1, 'role', null, null, null),
		(1, 10, NOW(), 1, 1, 'user_permission', null, null, null),
		(1, 11, NOW(), 1, 1, 'device_lifecycle', null, null, null),
		(1, 12, NOW(), 1, 1, 'device_management', null, null, null),
		(1, 13, NOW(), 1, 1, 'account', null, null, null),
		(1, 14, NOW(), 1, 1, 'account', null, null, null);

--changeset jreimann:26

CREATE TABLE atht_access_token (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,

  user_id 					BIGINT(21) 	  UNSIGNED NOT NULL,
  token_id					TEXT	      NOT NULL,
  expires_on				TIMESTAMP(3)  NOT NULL,

  optlock               	INT UNSIGNED,
  attributes             	TEXT,
  properties             	TEXT,

  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_access_token_scope_id ON atht_access_token (scope_id);
CREATE INDEX idx_atht_access_token_user_id ON atht_access_token (scope_id, user_id);

