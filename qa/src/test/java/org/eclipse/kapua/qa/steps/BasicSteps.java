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
package org.eclipse.kapua.qa.steps;

import static java.time.Duration.ofSeconds;

import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class BasicSteps {

    private static final double WAIT_MULTIPLIER = Double.parseDouble(System.getProperty("org.eclipse.kapua.qa.waitMultiplier", "1.0"));

    @When("I wait (\\d+) seconds?.*")
    public void waitSeconds(int seconds) throws InterruptedException {
        double effectiveSeconds = ((double) seconds) * WAIT_MULTIPLIER;
        Thread.sleep(ofSeconds((long) Math.ceil(effectiveSeconds)).toMillis());
    }

}
