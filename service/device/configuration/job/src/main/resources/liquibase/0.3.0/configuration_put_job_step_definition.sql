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

-- changeset configuration-job:1

INSERT INTO job_job_step_definition
VALUES (1,
  NULL,
  CURRENT_TIMESTAMP(),
  1,
  CURRENT_TIMESTAMP(),
  1,
  'Device Configuration Management Configuration',
  'Sends a configuration using the Device Configuration Management Service',
  'TARGET',
  NULL,
  'org.eclipse.kapua.service.device.management.configuration.job.DeviceConfigurationPutTargetProcessor',
        NULL,
        0,
        NULL,
        NULL
);

INSERT INTO job_job_step_definition_properties
VALUES
  (
    (SELECT id
    FROM job_job_step_definition
    WHERE name = 'Device Configuration Management Configuration'),
    'configuration',
    'org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration',
    NULL
  ),
  (
    SELECT id
    FROM job_job_step_definition
    WHERE name = 'Device Configuration Management Configuration',
    'timeout',
    'java.lang.Long',
    '30000'
  );
