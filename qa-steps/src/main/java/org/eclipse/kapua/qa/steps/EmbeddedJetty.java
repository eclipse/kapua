/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AllowSymLinkAliasChecker;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

public class EmbeddedJetty {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJetty.class);

    private static Server jetty;

    @Before(value = "@StartJetty")
    public void start() throws Exception {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
        jetty = new Server(address);
        logger.info("Starting Jetty " + jetty.toString());

        // Setup JMX
        MBeanContainer mbContainer = new MBeanContainer(
                ManagementFactory.getPlatformMBeanServer());
        jetty.addBean(mbContainer);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        //File warFile = new File("../rest-api/web/target/api.war");
        String warFileName = System.getProperty("jetty.war.file");
        File warFile = new File(warFileName);
        webapp.setWar(warFile.getAbsolutePath());
        webapp.addAliasCheck(new AllowSymLinkAliasChecker());

        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(jetty);
        classlist.addBefore(
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration" );

        webapp.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );

        jetty.setHandler(webapp);

        jetty.start();
        jetty.dumpStdErr();
        // Blocks
        //jetty.join();
    }

    @After(value = "@StopJetty")
    public void stop() throws Exception {
        logger.info("Stopping Jetty " + jetty.toString());

        jetty.stop();
    }

}
