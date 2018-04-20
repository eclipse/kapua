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
package org.eclipse.kapua.service.device.steps;

import static java.time.Instant.now;
import static org.eclipse.kapua.locator.KapuaLocator.getInstance;
import static org.eclipse.kapua.service.device.steps.With.withUserAccount;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.junit.Assert;

import com.google.inject.Inject;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class DataSteps {

    public static class MetricEntry {

        private final String key;
        private final String type;
        private final String value;

        public MetricEntry(final String key, final String type, final String value) {
            this.key = key;
            this.type = type;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    private final SimulatorDevice currentDevice;
    private final Session session;

    private String currentApplication;

    @Inject
    public DataSteps(final SimulatorDevice currentDevice, final Session session) {
        this.currentDevice = currentDevice;
        this.session = session;
    }

    @Given("I have a mock data application named \"(.*)\"")
    public void addMockDataApplication(final String applicationId) {
        Assert.assertFalse(currentDevice.isStarted());

        final MockDataApplication app = new MockDataApplication(applicationId);
        currentDevice.getMockApplications().put(applicationId, app);
    }

    @Given("I publish for the application \"(.*)\"")
    public void selectPublishApplication(final String applicationId) {
        currentApplication = applicationId;
    }

    @When("I publish on the topic \"(.*)\" timestamped now")
    public void publishMetric(final String topic, final List<MetricEntry> metrics) {

        final Map<String, Object> data = toData(metrics);

        final MockDataApplication app = getMockApplication(currentApplication);
        app.publishData(topic, now(), data);
    }

    private static Map<String, Object> toData(List<MetricEntry> metrics) {
        final Map<String, Object> data = new HashMap<>();

        for (final MetricEntry entry : metrics) {

            final String key = entry.getKey();
            final String stringValue = entry.getValue();
            final String type = entry.getType();

            switch (type.toUpperCase()) {
            case "STRING":
                data.put(key, stringValue);
                break;
            case "INT32":
                data.put(key, Integer.valueOf(stringValue));
                break;
            case "INT64":
                data.put(key, Long.valueOf(stringValue));
                break;
            case "DOUBLE":
                data.put(key, Double.parseDouble(stringValue));
                break;
            case "BOOLEAN":
                data.put(key, Boolean.valueOf(stringValue));
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown type: %s", type));
            }
        }
        return data;
    }

    private MockDataApplication getMockApplication(final String applicationId) {
        final MockDataApplication app = currentDevice.getMockApplications().get(applicationId);
        if (app == null) {
            throw new IllegalStateException(String.format("Application '%s' not found in current setup", applicationId));
        }
        return app;
    }

    @Then("I expect the number of messages for this device to be (\\d+)")
    public void expectNumberOfMessages(long numberOfMessages) throws Exception {
        final MessageStoreService service = getInstance().getService(MessageStoreService.class);
        session.withLogin(() -> {
            withUserAccount(currentDevice.getAccountName(), account -> {

                // create new query

                final MessageQueryImpl query = new MessageQueryImpl(account.getId());

                // filter for client only

                query.setPredicate(new TermPredicateImpl(MessageField.CLIENT_ID, currentDevice.getClientId()));

            // set query options
            query.setAskTotalCount(true);
            query.setLimit((int)numberOfMessages);

                // perform query

                final MessageListResult result = service.query(query);

                // eval just the size

                Assert.assertEquals(numberOfMessages, result.getSize());

                // eval the total count

                Assert.assertEquals(Long.valueOf(numberOfMessages), result.getTotalCount());

                // different approach -> same result

                Assert.assertEquals(numberOfMessages, service.count(query));
            });
        });
    }

    @Then("I expect the latest captured message on channel \"(.*)\" to have the metrics")
    public void testMessageData(final String topic, final List<MetricEntry> expectedMetrics) throws Exception {
        final MessageStoreService service = getInstance().getService(MessageStoreService.class);
        session.withLogin(() -> {
            withUserAccount(currentDevice.getAccountName(), account -> {

                // start a new query

                final MessageQueryImpl query = new MessageQueryImpl(account.getId());

                // query for client and channel

                final AndPredicateImpl and = new AndPredicateImpl();
                and.getPredicates().add(new TermPredicateImpl(MessageField.CLIENT_ID, currentDevice.getClientId()));
                and.getPredicates().add(new TermPredicateImpl(MessageField.CHANNEL, topic));
                query.setPredicate(and);

                // sort by captured time

                query.setSortFields(Arrays.asList(SortField.descending(MessageField.CAPTURED_ON.field())));

                // perform the query

                final MessageListResult result = service.query(query);

                Assert.assertEquals(1, result.getSize());

                // get the first item

                final DatastoreMessage message = result.getFirstItem();
                Assert.assertEquals(currentDevice.getClientId(), message.getClientId());

                // get payload structure

                final KapuaPayload payload = message.getPayload();

                // assert metrics data

                final Map<String, Object> properties = payload.getMetrics();
                Assert.assertEquals(toData(expectedMetrics), properties);
            });
        });
    }

    @When("^I refresh all indices$")
    public void refreshIndeces() throws Throwable {
        DatastoreMediator.getInstance().refreshAllIndexes();
    }

}
