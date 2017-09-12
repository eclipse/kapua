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

-- changeset device:1

ALTER TABLE dvc_device
	ADD CHECK scope_id >= 0;

ALTER TABLE dvc_device
	ADD CHECK id >= 0;

ALTER TABLE dvc_device
	ADD CHECK created_by >= 0;

ALTER TABLE dvc_device
	ADD CHECK modified_by >= 0;

ALTER TABLE dvc_device
	ADD CHECK group_id >= 0;

ALTER TABLE dvc_device
	ADD CHECK connection_id >= 0;

ALTER TABLE dvc_device
	ADD CHECK last_event_id >= 0;



