/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.client;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.assertj.core.api.Assertions;

import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.test.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ClientUtilsConvertDateTest extends Assert {

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void convertNullString() {
        DatastoreUtils.convertToCorrectType(DatastoreUtils.CLIENT_METRIC_TYPE_DATE_ACRONYM, null);
    }

    @Test
    public void convertValidString() {
        Assertions.assertThat(DatastoreUtils.convertToCorrectType(DatastoreUtils.CLIENT_METRIC_TYPE_DATE_ACRONYM, "2017-01-02T12:34:56.123Z"))
                .isInstanceOf(Date.class)
                .isEqualTo(Date.from(ZonedDateTime.of(2017, 1, 2, 12, 34, 56, 123_000_000, ZoneOffset.UTC).toInstant()));
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void convertWrongString() {
        DatastoreUtils.convertToCorrectType(DatastoreUtils.CLIENT_METRIC_TYPE_DATE_ACRONYM, "01-02-2017T12:34:56.123Z");
    }

    @Test(expected = java.time.format.DateTimeParseException.class)
    public void convertEmptyString() {
        DatastoreUtils.convertToCorrectType(DatastoreUtils.CLIENT_METRIC_TYPE_DATE_ACRONYM, "");
    }
}
