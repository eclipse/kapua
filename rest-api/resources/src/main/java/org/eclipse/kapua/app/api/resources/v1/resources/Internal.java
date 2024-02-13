/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.grapher.graphviz.GraphvizGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

@Path("_internal")
public class Internal extends AbstractKapuaResource {

    @GET
    @Path("_wiring")
    @Produces({MediaType.TEXT_PLAIN})
    public StreamingOutput fetchGraph() throws KapuaException {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                final PrintWriter out = new PrintWriter(output);
                Injector injector = Guice.createInjector(new GraphvizModule());
                GraphvizGrapher grapher = injector.getInstance(GraphvizGrapher.class);
                grapher.setOut(out);
                grapher.setRankdir("TB");
                grapher.graph(((GuiceLocatorImpl) KapuaLocator.getInstance()).getInjector());
            }
        };
    }
}
