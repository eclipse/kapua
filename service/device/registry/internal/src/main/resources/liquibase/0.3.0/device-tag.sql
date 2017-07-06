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

-- changeset device_tag:1

CREATE TABLE dvc_device_tag (
  device_id                 BIGINT(21) 	  UNSIGNED NOT NULL,
  tag_id               		BIGINT(21) 	  UNSIGNED NOT NULL,
  
  PRIMARY KEY (device_id, tag_id),
  
  FOREIGN KEY (device_id) REFERENCES dvc_device(id) ON DELETE CASCADE
 
) DEFAULT CHARSET=utf8;