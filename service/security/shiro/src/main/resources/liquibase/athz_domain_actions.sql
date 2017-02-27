-- *******************************************************************************
-- Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
--
-- All rights reserved. This program and the accompanying materials
-- are made available under the terms of the Eclipse Public License v1.0
-- which accompanies this distribution, and is available at
-- http://www.eclipse.org/legal/epl-v10.html
--
-- Contributors:
--     Eurotech - initial API and implementation
-- *******************************************************************************

--liquibase formatted sql

--changeset domain_actions:1

CREATE TABLE athz_domain_actions (
  domain_id                 BIGINT(21) 	  UNSIGNED NOT NULL,
  action					VARCHAR(255)  NOT NULL,
  
  PRIMARY KEY (domain_id, action)
 
) DEFAULT CHARSET=utf8;


INSERT INTO athz_domain_actions
	VALUES
		(1, 'read'),
		(1, 'write'),
		(1, 'delete'),

		(2, 'connect'),

		(3, 'read'),
		(3, 'write'),
		(3, 'delete'),

		(4, 'read'),
		(4, 'write'),
		(4, 'delete'),

		(5, 'read'),
		(5, 'write'),
		(5, 'delete'),

		(6, 'read'),
		(6, 'write'),
		(6, 'delete'),
		(6, 'execute'),
		(6, 'connect'),

		(7, 'read'),
		(7, 'write'),
		(7, 'delete'),

		(8, 'read'),
		(8, 'write'),
		(8, 'delete'),

		(9, 'read'),
		(9, 'write'),
		(9, 'delete'),

		(10, 'read'),
		(10, 'write'),
		(10, 'delete'),

		(11, 'read'),
		(11, 'write'),
		(11, 'delete'),

		(12, 'read'),
		(12, 'write'),
		(12, 'delete'),

		(13, 'read'),
		(13, 'write'),
		(13, 'delete'),

		(14, 'read'),
		(14, 'write'),
		(14, 'delete'),

		(15, 'read'),
		(15, 'write'),
		(15, 'delete');