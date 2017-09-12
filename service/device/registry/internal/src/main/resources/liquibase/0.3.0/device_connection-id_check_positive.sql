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

-- changeset device_connection:1

ALTER TABLE dvc_device_connection
	ADD CHECK scope_id >= 0;

ALTER TABLE dvc_device_connection
	ADD CHECK id >= 0;

ALTER TABLE dvc_device_connection
	ADD CHECK created_by >= 0;

ALTER TABLE dvc_device_connection
	ADD CHECK modified_by >= 0;

ALTER TABLE dvc_device_connection
	ADD CHECK reserved_user_id >= 0;



