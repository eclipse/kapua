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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

public class EsUtilsConvertDateTest {

    @Test
    public void convertNull1() {
        Assert.assertNull(EsUtils.convertToKapuaObject("date", null));
    }

    @Test
    public void convertWithMillis1() {
        Assertions.assertThat(EsUtils.convertToKapuaObject("date", "2017-01-02T12:34:56.123Z"))
                .isInstanceOf(Date.class)
                .isEqualTo(Date.from(ZonedDateTime.of(2017, 1, 2, 12, 34, 56, 123_000_000, ZoneOffset.UTC).toInstant()));
    }

    @Test
    public void convertWithMillis2() {
        Assertions.assertThat(EsUtils.convertToKapuaObject("date", "2017-01-02T12:34:56.123"))
                .isInstanceOf(Date.class)
                .isEqualTo(Date.from(ZonedDateTime.of(2017, 1, 2, 12, 34, 56, 123_000_000, ZoneOffset.UTC).toInstant()));
    }

    @Test
    public void convertNoMillis1() {
        Assertions.assertThat(EsUtils.convertToKapuaObject("date", "2017-01-02T13:34:56Z"))
                .isInstanceOf(Date.class)
                .isEqualTo(Date.from(ZonedDateTime.of(2017, 1, 2, 13, 34, 56, 0, ZoneOffset.UTC).toInstant()));
    }

    @Test
    public void convertNoMillis2() {
        Assertions.assertThat(EsUtils.convertToKapuaObject("date", "2017-01-02T13:34:56"))
                .isInstanceOf(Date.class)
                .isEqualTo(Date.from(ZonedDateTime.of(2017, 1, 2, 13, 34, 56, 0, ZoneOffset.UTC).toInstant()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertWrongFormat() {
        EsUtils.convertToKapuaObject("date", "");
    }
}
