/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.tag.steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagAttributes;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.service.tag.internal.TagEntityManagerFactory;
import org.eclipse.kapua.service.tag.internal.TagFactoryImpl;
import org.eclipse.kapua.service.tag.internal.TagServiceImpl;
import org.eclipse.kapua.test.MockedLocator;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Gherkin steps used in TagService.feature scenarios.
 */
@ScenarioScoped
public class TagServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceSteps.class);

    /**
     * Tag service.
     */
    private TagService tagService;
    private TagFactory tagFactory;

    private DBHelper database;

    @Inject
    public TagServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for sevice unit tests.
     */
    private void setupDI() {

        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                bind(PermissionFactory.class).toInstance(Mockito.mock(PermissionFactory.class));
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // Inject actual user service related services
                TagEntityManagerFactory tagEntityManagerFactory = TagEntityManagerFactory.getInstance();
                bind(TagEntityManagerFactory.class).toInstance(tagEntityManagerFactory);
                bind(TagService.class).toInstance(new TagServiceImpl());
                bind(TagFactory.class).toInstance(new TagFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        tagService = locator.getService(TagService.class);
        tagFactory = locator.getFactory(TagFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
    }

    @After
    public void afterScenario() {

        // Clean up the database
        try {
            logger.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                database.deleteAll();
                SecurityUtils.getSubject().logout();
            } else {
                database.dropAll();
                database.close();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    @Given("^I configure the tag service$")
    public void setConfigurationValue(List<CucConfig> testConfigs)
            throws Exception {

        Account lastAcc = (Account) stepData.get("LastAccount");
        KapuaId scopeId = SYS_SCOPE_ID;
        KapuaId parentId = SYS_SCOPE_ID;
        if (lastAcc != null) {
            scopeId = lastAcc.getId();
            parentId = lastAcc.getScopeId();
        }
        Map<String, Object> valueMap = new HashMap<>();

        for (CucConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }
        try {
            primeException();
            tagService.setConfigValues(scopeId, parentId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^Tag with name \"([^\"]*)\"$")
    public void tagWithName(String tagName) throws Throwable {

        TagCreator tagCreator = tagCreatorCreator(tagName);
        tagService.create(tagCreator);
    }

    @When("^Tag with name \"([^\"]*)\" is searched$")
    public void tagWithNameIfSearched(String tagName) throws Throwable {

        KapuaQuery<Tag> query = tagFactory.newQuery(SYS_SCOPE_ID);
        query.setPredicate(new AttributePredicateImpl<String>(TagAttributes.NAME, tagName, AttributePredicate.Operator.EQUAL));
        TagListResult queryResult = tagService.query(query);
        Tag foundTag = queryResult.getFirstItem();
        stepData.put("tag", foundTag);
        stepData.put("queryResult", queryResult);
    }

    @Then("^Tag with name \"([^\"]*)\" is found$")
    public void tagWithNameIsFound(String tagName) {

        Tag foundTag = (Tag) stepData.get("tag");
        Assert.assertEquals(tagName, foundTag.getName());
    }

    @Then("^Tag with name \"([^\"]*)\" is found and deleted$")
    public void tagWithNameIsDeleted(String tagName) throws Throwable {

        Tag foundTag = (Tag) stepData.get("tag");
        TagListResult queryResult = (TagListResult) stepData.get("queryResult");
        tagService.delete(foundTag.getScopeId(), foundTag.getId());
        queryResult.clearItems();
        foundTag = queryResult.getFirstItem();
        Assert.assertEquals(null,foundTag);
    }

    /**
     * Create TagCreator for creating tag with specified name.
     *
     * @param tagName name of tag
     * @return tag creator for tag with specified name
     */
    private TagCreator tagCreatorCreator(String tagName) {

        TagCreator tagCreator = tagFactory.newCreator(SYS_SCOPE_ID);
        tagCreator.setName(tagName);

        return tagCreator;
    }
}
