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

-- changeset account-foreignkeys:1

ALTER TABLE dvc_device_tag
	ADD CONSTRAINT fk_dvc_device_tag_tagId
		FOREIGN KEY (tag_id) REFERENCES tag_tag(id)
			ON DELETE CASCADE;



