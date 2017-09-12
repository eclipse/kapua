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

-- changeset group-configuration:1

INSERT INTO sys_configuration (
  SCOPE_ID,
  PID,
  CONFIGURATIONS,
  CREATED_ON,
  CREATED_BY,
  MODIFIED_ON,
  MODIFIED_BY,
  OPTLOCK,
  ATTRIBUTES,
  PROPERTIES)
VALUES (1,
        'org.eclipse.kapua.service.authorization.group.GroupService',
        CONCAT('#', CURRENT_TIMESTAMP(), CHAR(13), CHAR(10),
        'maxNumberChildEntities=0', CHAR(13), CHAR(10),
        'infiniteChildEntities=true'),
  CURRENT_TIMESTAMP(),
  1,
  CURRENT_TIMESTAMP(),
  1,
  0,
  null,
  null);