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

-- changeset credential-foreignkeys:1

ALTER TABLE atht_credential
	ADD CONSTRAINT fk_atht_credential_scopeId
		FOREIGN KEY (scope_id) REFERENCES act_account(id)
			ON DELETE CASCADE;
			
ALTER TABLE atht_credential
	ADD CONSTRAINT fk_atht_credential_userId
		FOREIGN KEY (user_id) REFERENCES usr_user(id)
			ON DELETE CASCADE;			
