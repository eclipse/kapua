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

-- changeset job:1

CREATE TABLE job_job_execution (
  scope_id          			BIGINT(21) 	  	UNSIGNED NOT NULL,
  id                        	BIGINT(21)		UNSIGNED NOT NULL,
  created_on                	TIMESTAMP(3)		DEFAULT 0,
  created_by                	BIGINT(21) 	  	UNSIGNED NOT NULL,
  modified_on               	TIMESTAMP(3)		NOT NULL,
  modified_by               	BIGINT(21)		UNSIGNED NOT NULL,
  
  job_id          		   	BIGINT(21) 	  	UNSIGNED NOT NULL,
  started_on                	TIMESTAMP(3) 	DEFAULT 0,				
  ended_on					TIMESTAMP(3)		DEFAULT 0,
  
  optlock                   	INT UNSIGNED,
  attributes					TEXT,
  properties                	TEXT,
  
  PRIMARY KEY (id),
  
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE INDEX idx_job_execution_scope_id ON job_job_execution (scope_id);