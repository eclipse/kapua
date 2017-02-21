/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/

CREATE TABLE act_account (
  scope_id          		 BIGINT(21) 	UNSIGNED,
  id                         BIGINT(21) 	UNSIGNED NOT NULL,
  name                       VARCHAR(255)  	NOT NULL,
 
  created_on             	 TIMESTAMP(3)  	NOT NULL,
  created_by_type			 VARCHAR(64)   	NOT NULL,
  created_by_id            	 BIGINT(21)    	UNSIGNED NOT NULL,
  
  modified_on            	 TIMESTAMP(3)	NOT NULL,
  modified_by_type			 VARCHAR(64)   	NOT NULL,
  modified_by_id             BIGINT(21)    	UNSIGNED NOT NULL,
  
  org_name                   VARCHAR(255) 	NOT NULL,
  org_person_name            VARCHAR(255),
  org_email                  VARCHAR(255) 	NOT NULL,
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


CREATE TABLE atht_access_token (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,
  
  subject_type				VARCHAR(64)   NOT NULL,
  subject_id				BIGINT(21)	  UNSIGNED,
  
  credential_id				BIGINT(21)	  UNSIGNED NOT NULL,
  token_id					TEXT	      NOT NULL,
  expires_on				TIMESTAMP(3)  NOT NULL,
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  
  
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_access_token_scope_id ON atht_access_token (scope_id);
CREATE INDEX idx_atht_access_token_user_id ON atht_access_token (scope_id, subject_type, subject_id);


CREATE TABLE atht_credential (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,
  
  subject_type				VARCHAR(64)   NOT NULL,
  subject_id				BIGINT(21)	  UNSIGNED,

  type						VARCHAR(64)	  NOT NULL,
  key						VARCHAR(255)  NOT NULL,
  secret				 	VARCHAR(255)  NOT NULL,
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  
  
  PRIMARY KEY (id)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_atht_credential_scope_id ON atht_credential (scope_id);
CREATE INDEX idx_atht_credential_subject_type_subject_id ON atht_credential (scope_id, subject_type, subject_id);
CREATE INDEX idx_atht_credential_type_credential_key ON atht_credential (type, key);


CREATE TABLE athz_access_info (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,
  
  subject_type				VARCHAR(64)   NOT NULL,
  subject_id				BIGINT(21)	  UNSIGNED,

  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,
  
  PRIMARY KEY (id)
  
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_scopeId_userId ON athz_access_info (scope_id, subject_type, subject_id);


CREATE TABLE athz_access_permission (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  access_info_id			BIGINT(21) 	  UNSIGNED NOT NULL,
  
  domain					VARCHAR(64),
  action					VARCHAR(64),
  target_scope_id			BIGINT(21)	  UNSIGNED,
  group_id             	    BIGINT(21) 	  UNSIGNED,
    
  PRIMARY KEY (id),
--  FOREIGN KEY (access_id) REFERENCES athz_access_info(id) ON DELETE CASCADE
  
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_scopeId_accessId_domain_action_targetScopeId_groupId ON athz_access_permission (scope_id, access_info_id, domain, action, target_scope_id, group_id);


CREATE TABLE athz_access_role (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  access_info_id			BIGINT(21) 	  UNSIGNED NOT NULL,
  role_id					BIGINT(21) 	  UNSIGNED NOT NULL,
    
  PRIMARY KEY (id),
--  FOREIGN KEY (access_id) REFERENCES athz_access_info(id) ON DELETE CASCADE,
--  FOREIGN KEY (role_id) REFERENCES athz_role(id) ON DELETE RESTRICT
  
) DEFAULT CHARSET=utf8;



CREATE UNIQUE INDEX idx_scopeId_accessId_roleId ON athz_access_role (scope_id, access_info_id, role_id);


CREATE TABLE athz_domain (
  scope_id             		BIGINT(21) 	  UNSIGNED,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,

  name 						VARCHAR(255)  NOT NULL,
  serviceName 				VARCHAR(1023) NOT NULL,
    
  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_domain_name ON athz_domain (name);



CREATE TABLE athz_domain_actions (
  domain_id                 BIGINT(21) 	  UNSIGNED NOT NULL,
  action					VARCHAR(255)  NOT NULL,
  
  PRIMARY KEY (domain_id, action)
 
) DEFAULT CHARSET=utf8;


CREATE TABLE athz_group (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
   
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,

  name 						VARCHAR(255)  NOT NULL,
  
  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,
  
  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_group_name ON athz_group (scope_id, name);


CREATE TABLE athz_role (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
   
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,

  name 						VARCHAR(255)  NOT NULL,
  
  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,
  
  PRIMARY KEY (id)

) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_role_name ON athz_role (scope_id, name);


CREATE TABLE athz_role_permission (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  role_id             	    BIGINT(21) 	  UNSIGNED,
  
  domain					VARCHAR(64),
  action					VARCHAR(64),
  target_scope_id		    BIGINT(21)	  UNSIGNED,
  group_id             	    BIGINT(21) 	  UNSIGNED,
  
  PRIMARY KEY (id),
--  FOREIGN KEY (role_id) REFERENCES athz_role(id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8;

CREATE UNIQUE INDEX idx_role_permission_scope_id ON athz_role_permission (role_id, domain, action, target_scope_id, group_id);


CREATE TABLE dvc_device_connection (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,
  
  connection_status		    VARCHAR(20)   NOT NULL,
  client_id					VARCHAR(255)  NOT NULL,
  credential_id  			BIGINT(21)    UNSIGNED, 
  last_credential_id		BIGINT(21)    UNSIGNED,
  protocol       			VARCHAR(64),
  client_ip      			VARCHAR(255),
  server_ip      			VARCHAR(255), 
  
  optlock                   INT UNSIGNED,
  attributes				TEXT,
  properties                TEXT,

  PRIMARY KEY (scope_id, id)   -- primary key needs to include the partitioning key
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_connection_status_id ON dvc_device_connection (scope_id, id, connection_status);


CREATE TABLE dvc_device (
  scope_id             	    BIGINT(21) 	    UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	    UNSIGNED NOT NULL,
  
  created_on             	TIMESTAMP(3) 	NOT NULL,
  created_by_type			VARCHAR(64)   	NOT NULL,
  created_by_id            	BIGINT(21)    	UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  	NOT NULL,
  modified_by_type			VARCHAR(64)   	NOT NULL,
  modified_by_id            BIGINT(21)    	UNSIGNED NOT NULL,

  group_id             	    BIGINT(21) 	    UNSIGNED,
  
  client_id                 VARCHAR(255)    NOT NULL,
  connection_id             BIGINT(21) 	    UNSIGNED NULL,
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


CREATE TABLE dvc_device_event (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,

  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
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


CREATE TABLE sys_configuration (
  scope_id          		 BIGINT(21) 	UNSIGNED,
  id                         BIGINT(21) 	UNSIGNED NOT NULL,
  
  created_on             	 TIMESTAMP(3)  	NOT NULL,
  created_by_type			 VARCHAR(64)   	NOT NULL,
  created_by_id            	 BIGINT(21)    	UNSIGNED NOT NULL,
  
  modified_on            	 TIMESTAMP(3)	NOT NULL,
  modified_by_type			 VARCHAR(64)   	NOT NULL,
  modified_by_id             BIGINT(21)    	UNSIGNED NOT NULL,
  
  pid						 VARCHAR(255) 	NOT NULL,
  configurations			 TEXT,

  optlock                    INT UNSIGNED,
  attributes				 TEXT,
  properties                 TEXT,
  PRIMARY KEY  (id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX idx_configurationScopeId ON sys_configuration (scope_id);

CREATE TABLE usr_user (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  name               	    VARCHAR(255)  NOT NULL,
  
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by_type			VARCHAR(64)   NOT NULL,
  created_by_id            	BIGINT(21)    UNSIGNED NOT NULL,
  
  modified_on            	TIMESTAMP(3)  NOT NULL,
  modified_by_type			VARCHAR(64)   NOT NULL,
  modified_by_id            BIGINT(21)    UNSIGNED NOT NULL,
  
  status                 	VARCHAR(64)   NOT NULL DEFAULT 'ENABLED',
  display_name           	VARCHAR(255),
  email                  	VARCHAR(255),
  phone_number           	VARCHAR(64),
  user_type					VARCHAR(64)   NOT NULL DEFAULT 'INTERNAL',
  external_id				VARCHAR(255),
  
  optlock               	INT UNSIGNED,
  attributes             	TEXT,  
  properties             	TEXT,  

  PRIMARY KEY (id),
  CONSTRAINT usr_uc_name UNIQUE (name)
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_user_scope_id ON usr_user (scope_id);
