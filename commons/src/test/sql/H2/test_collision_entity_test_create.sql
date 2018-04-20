/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *  
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/

CREATE TABLE collision_entity_test (
  scope_id             		BIGINT(21) 	  UNSIGNED NOT NULL,
  id                     	BIGINT(21) 	  UNSIGNED NOT NULL,
  name               	    VARCHAR(255)  NOT NULL,
  created_on             	TIMESTAMP(3)  NOT NULL,
  created_by             	BIGINT(21)    UNSIGNED NOT NULL,
  modified_on            	TIMESTAMP(3),
  modified_by            	BIGINT(21)    UNSIGNED,
  optlock                   INT UNSIGNED,
  test_field             	VARCHAR(255) NOT NULL UNIQUE,
  attributes				TEXT,
  properties                TEXT,

  PRIMARY KEY (id),

  CHECK scope_id >= 0,
  CHECK id >= 0,
  CHECK created_by >= 0,
  CHECK modified_by >= 0

) ENGINE = InnoDB DEFAULT CHARSET = utf8;

CREATE INDEX idx_collision_entity_test_scope_id ON collision_entity_test (scope_id);
