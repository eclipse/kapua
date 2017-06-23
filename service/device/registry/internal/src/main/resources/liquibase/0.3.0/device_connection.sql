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
	ADD COLUMN  user_coupling_mode VARCHAR(20) NOT NULL DEFAULT 'LOOSE';

ALTER TABLE dvc_device_connection 
	ADD COLUMN  reserved_user_id BIGINT(21) UNSIGNED;
	
