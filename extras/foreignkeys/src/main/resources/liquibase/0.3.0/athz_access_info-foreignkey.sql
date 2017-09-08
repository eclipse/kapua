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

-- changeset access_info-foreignkeys:1

ALTER TABLE athz_access_info
	ADD CONSTRAINT fk_athz_access_info_scopeId
		FOREIGN KEY (scope_id) REFERENCES act_account(id)
			ON DELETE CASCADE;
			
ALTER TABLE athz_access_info
	ADD CONSTRAINT fk_athz_access_info_userId
		FOREIGN KEY (user_id) REFERENCES usr_user(id)
			ON DELETE CASCADE;			
