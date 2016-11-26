package org.eclipse.kapua.service.user.internal;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        plugin = {"pretty", "html:target/cucumber",
                  "json:target/cucumber.json"},
        monochrome=true)
public class RunTest { }
