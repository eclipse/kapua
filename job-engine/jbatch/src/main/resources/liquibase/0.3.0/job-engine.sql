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

-- changeset job-engine:1

CREATE TABLE CHECKPOINTDATA(
	id VARCHAR(512),
	obj BLOB
);

CREATE TABLE JOBINSTANCEDATA(
	jobinstanceid BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(512),
	apptag VARCHAR(512),

	PRIMARY KEY (jobinstanceid)
);

CREATE TABLE EXECUTIONINSTANCEDATA(
	jobexecid BIGINT NOT NULL AUTO_INCREMENT,
	jobinstanceid BIGINT,
	createtime TIMESTAMP,
	starttime TIMESTAMP,
	endtime TIMESTAMP,
	updatetime TIMESTAMP,
	parameters BLOB,
	batchstatus VARCHAR(512),
	exitstatus VARCHAR(512), 

	PRIMARY KEY (jobexecid),

	CONSTRAINT JOBINST_JOBEXEC_FK 
		FOREIGN KEY (jobinstanceid) 
			REFERENCES JOBINSTANCEDATA (jobinstanceid) 
				ON DELETE CASCADE
);

CREATE TABLE JOBSTATUS (
	id BIGINT,
	obj BLOB,

	PRIMARY KEY (id),

	CONSTRAINT JOBSTATUS_JOBINST_FK 
		FOREIGN KEY (id) 
			REFERENCES JOBINSTANCEDATA (jobinstanceid) 
				ON DELETE CASCADE
);

CREATE TABLE STEPEXECUTIONINSTANCEDATA(
	stepexecid BIGINT NOT NULL AUTO_INCREMENT,
	jobexecid BIGINT,
	batchstatus VARCHAR(512),
	exitstatus VARCHAR(512),
	stepname VARCHAR(512),
	readcount INTEGER,
	writecount INTEGER,
	commitcount INTEGER,
	rollbackcount INTEGER,
	readskipcount INTEGER,
	processskipcount INTEGER,
	filtercount INTEGER,
	writeskipcount INTEGER,
	startTime TIMESTAMP(3), 
	endTime TIMESTAMP(3),
	persistentData BLOB,

	PRIMARY KEY (stepexecid),

	CONSTRAINT JOBEXEC_STEPEXEC_FK 
		FOREIGN KEY (jobexecid) 
			REFERENCES EXECUTIONINSTANCEDATA (jobexecid) 
				ON DELETE CASCADE
);

CREATE TABLE STEPSTATUS (
	id BIGINT,
	obj BLOB,

	PRIMARY KEY (id),

	CONSTRAINT STEPSTATUS_STEPEXEC_FK 
		FOREIGN KEY (id) 
			REFERENCES STEPEXECUTIONINSTANCEDATA (stepexecid) 
				ON DELETE CASCADE
);