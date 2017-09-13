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

-- changeset role_permission:1

ALTER TABLE athz_role_permission
	ADD CHECK scope_id >= 0;

ALTER TABLE athz_role_permission
	ADD CHECK id >= 0;

ALTER TABLE athz_role_permission
	ADD CHECK created_by >= 0;

ALTER TABLE athz_role_permission
	ADD CHECK role_id >= 0;

ALTER TABLE athz_role_permission
	ADD CHECK target_scope_id >= 0;

ALTER TABLE athz_role_permission
	ADD CHECK group_id >= 0;


