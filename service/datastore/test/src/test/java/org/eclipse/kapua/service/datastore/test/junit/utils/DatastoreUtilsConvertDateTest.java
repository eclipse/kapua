/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.test.junit.utils;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;


@Category(JUnitTests.class)
public class DatastoreUtilsConvertDateTest {

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
