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

--changeset dev_group:1

CREATE TABLE dvc_dev_group (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  dev_id               	    BIGINT(21)    UNSIGNED NOT NULL,
  group_id                  BIGINT(21)    UNSIGNED NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,
  optlock               	INT UNSIGNED,
  attributes             	TEXT,
  properties	                TEXT,	
 
  PRIMARY KEY (id),
  FOREIGN KEY (scope_id) REFERENCES act_account(id) ON DELETE RESTRICT,
  FOREIGN KEY (dev_id) REFERENCES dvc_device(id) ON DELETE RESTRICT,
  FOREIGN KEY (group_id) REFERENCES athz_group(id) ON DELETE RESTRICT,
  
) DEFAULT CHARSET=utf8;

CREATE INDEX idx_devgroup_scope_id ON dvc_dev_group (scope_id);
