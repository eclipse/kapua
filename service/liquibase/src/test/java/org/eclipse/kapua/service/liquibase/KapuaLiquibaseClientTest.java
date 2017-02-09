/*******************************************************************************
 * Copyright (c) 2011, 2017 Red Hat and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.kapua.service.liquibase;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KapuaLiquibaseClientTest {

    @Test
    public void shouldExecuteScriptFromClasspath() {
        List<String> updateOutput = new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "", "").update();
        assertThat(updateOutput).contains("-- Lock Database");
    }

}
