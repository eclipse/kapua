/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.about.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.about.shared.model.GwtAboutDependency;
import org.eclipse.kapua.app.console.module.about.shared.model.GwtAboutInformation;
import org.eclipse.kapua.app.console.module.about.shared.service.GwtAboutService;
import org.eclipse.kapua.commons.about.AboutEntry;
import org.eclipse.kapua.commons.about.AboutEntry.License;
import org.eclipse.kapua.commons.about.AboutScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.html.HtmlEscapers;

public class GwtAboutServiceImpl extends KapuaRemoteServiceServlet implements GwtAboutService {

    private static final long serialVersionUID = -1121646317524473761L;

    private static final Logger logger = LoggerFactory.getLogger(GwtAboutServiceImpl.class);

    @Override
    public GwtAboutInformation getInformation() {

        final AboutScanner scanner = AboutScanner.scan();
        final GwtAboutInformation result = new GwtAboutInformation();

        // convertAccessPermissionCreator to UI structures

        final List<GwtAboutDependency> dependencies = new ArrayList<GwtAboutDependency>();

        for (final AboutEntry entry : scanner.getEntries()) {
            dependencies.add(map(entry));
        }

        // set result list

        result.setDependencies(dependencies);

        // return result

        logger.debug("Found {} about entries", dependencies.size());

        return result;
    }

    private GwtAboutDependency map(AboutEntry entry) {
        if (entry == null) {
            return null;
        }

        final GwtAboutDependency result = new GwtAboutDependency();
        result.setId(entry.getId());
        result.setName(entry.getName());
        result.setVersion(entry.getVersion());

        final String notice = entry.getNotice();
        if (notice != null) {
            if (notice.contains("</html>")) {
                result.setNotice(HtmlEscapers.htmlEscaper().escape(notice));
                result.setNoticeMimeType("text/html");
            } else {
                result.setNotice(notice);
                result.setNoticeMimeType("text/plain");
            }
        }

        if (entry.getLicense().getUrl() != null) {
            final String url = entry.getLicense().getUrl().toString();
            result.setLicenseUrl(url);
            result.setLicense(url);
        }
        if (entry.getLicense().getName() != null) {
            result.setLicense(entry.getLicense().getName());
        }
        if (entry.getLicense().getName() == null && entry.getLicense().getUrl() == null) {
            // only set text when we have no more precise information
            result.setLicense("See details");
            result.setLicenseText(entry.getLicense().getText());
        }
        if (result.getNotice() != null && entry.getLicense() == License.UNKNOWN) {
            // show "see details" when the license is unknown but we have some notice document
            result.setLicense("See details");
        }

        return result;
    }

    public GwtAboutDependency dummyGwtAboutDependency() {
        return null;
    }
}
