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

-- changeset device_connection:1

ALTER TABLE dvc_device_connection
	ADD COLUMN  allow_user_change BOOLEAN NOT NULL;
	
UPDATE dvc_device_connection
	SET allow_user_change = false;

ALTER TABLE dvc_device_connection 
	ADD COLUMN  user_coupling_mode VARCHAR(20) NOT NULL;
	
UPDATE dvc_device_connection
	SET user_coupling_mode = 'INHERITED';

ALTER TABLE dvc_device_connection 
	ADD COLUMN  reserved_user_id BIGINT(21) UNSIGNED;

UPDATE dvc_device_connection
	SET reserved_user_id = NULL;

CREATE UNIQUE INDEX idx_device_connection_reserved_user_id ON dvc_device_connection (scope_id, reserved_user_id);