/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.service.tag.internal.TagFactoryImpl;
import org.eclipse.kapua.service.tag.internal.TagPredicates;
import org.eclipse.kapua.service.user.steps.TestConfig;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Implementation of Gherkin steps used in TagService.feature scenarios.
 */
@ScenarioScoped
public class TagServiceSteps {

    private static final KapuaEid DEFAULT_SCOPE_ID = new KapuaEid(BigInteger.valueOf(1L));

    /**
     * Tag service.
     */
    private static TagService tagService;

    /**
     * Inter step data scratchpad.
     */
    private StepData stepData;

    @Inject
    public TagServiceSteps(StepData stepData) { 

        this.stepData = stepData;
    }

    @Before
    public void tagStepsBefore() {

        KapuaLocator locator = KapuaLocator.getInstance();
        tagService = locator.getService(TagService.class);
        stepData.put("LastAccount", null);
    }

    @Given("^Tag Service configuration$")
    public void setConfigurationValue(List<TestConfig> testConfigs)
            throws KapuaException {

        Account lastAcc = (Account) stepData.get("LastAccount");
        KapuaId scopeId = DEFAULT_SCOPE_ID;
        KapuaId parentId = DEFAULT_SCOPE_ID;
        if (lastAcc != null) {
            scopeId = lastAcc.getId();
            parentId = lastAcc.getScopeId();
        }
        Map<String, Object> valueMap = new HashMap<>();

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }
        try {
            stepData.put("isException", false);
            tagService.setConfigValues(scopeId, parentId, valueMap);
        } catch (KapuaException ke) {
            stepData.put("isException", true);
            stepData.put("exception", ke);
        }
    }

    @Given("^Tag with name \"([^\"]*)\"$")
    public void tagWithName(String tagName) throws Throwable {

        TagCreator tagCreator = tagCreatorCreator(tagName);
        tagService.create(tagCreator);
    }

    @When("^Tag with name \"([^\"]*)\" is searched$")
    public void tagWithNameIfSearched(String tagName) throws Throwable {

        KapuaQuery<Tag> query = new TagFactoryImpl().newQuery(DEFAULT_SCOPE_ID);
        query.setPredicate(new AttributePredicate<String>(TagPredicates.NAME, tagName, KapuaAttributePredicate.Operator.EQUAL));
        TagListResult queryResult = tagService.query(query);
        Tag foundTag = queryResult.getFirstItem();
        stepData.put("tag", foundTag);
        stepData.put("queryResult", queryResult);
    }

    @Then("^Tag with name \"([^\"]*)\" is found$")
    public void tagWithNameIsFound(String tagName) throws Throwable {

        Tag foundTag = (Tag) stepData.get("tag");
        assertEquals(tagName, foundTag.getName());
    }

    @Then("^Tag with name \"([^\"]*)\" is found and deleted$")
    public void tagWithNameIsDeleted(String tagName) throws Throwable {

        Tag foundTag = (Tag) stepData.get("tag");
        TagListResult queryResult = (TagListResult) stepData.get("queryResult");
        tagService.delete(foundTag.getScopeId(), foundTag.getId());
        queryResult.clearItems();
        foundTag = queryResult.getFirstItem();
        assertEquals(null,foundTag);
    }

    /**
     * Create TagCreator for creating tag with specified name.
     *
     * @param tagName name of tag
     * @return tag creator for tag with specified name
     */
    private TagCreator tagCreatorCreator(String tagName) {

        TagCreator tagCreator = new TagFactoryImpl().newCreator(DEFAULT_SCOPE_ID);
        tagCreator.setName(tagName);

        return tagCreator;
    }
}
