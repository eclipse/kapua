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

-- changeset hekonsek:1 

CREATE TABLE tst_liquibase (
  id                         BIGINT(21) 	  UNSIGNED NOT NULL,

  PRIMARY KEY (id),

  CHECK  id >= 0
  
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
