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

-- changeset bundle-job:1

INSERT INTO job_job_step_definition
VALUES (1,
  NULL,
  CURRENT_TIMESTAMP(),
  1,
  CURRENT_TIMESTAMP(),
  1,
  'Device Bundle Management Stop',
  'Stops a bundle using the Device Bundle Management Service',
  'TARGET',
  NULL,
  'org.eclipse.kapua.service.device.management.bundle.job.DeviceBundleStopTargetProcessor',
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
    WHERE name = 'Device Bundle Management Stop'),
    'bundleId',
    'java.lang.String',
    NULL
  ),
  (
    (SELECT id
    FROM job_job_step_definition
    WHERE name = 'Device Bundle Management Stop'),
    'timeout',
    'java.lang.Long',
    '30000'
  );
