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

import java.io.IOException;

import org.eclipse.kapua.qa.steps.EmbeddedElasticsearch;
import org.eclipse.kapua.service.datastore.client.ClientException;

import com.google.inject.Inject;

import cucumber.api.java.en.When;

public class ElasticsearchSteps {

    private EmbeddedElasticsearch elasticsearch;

    @Inject
    public ElasticsearchSteps(final EmbeddedElasticsearch elasticsearch) {
        this.elasticsearch = elasticsearch;
    }

    @When("I refresh all indices")
    public void refreshAllIndices() throws IOException, ClientException {
        this.elasticsearch.refresh();
    }
}
