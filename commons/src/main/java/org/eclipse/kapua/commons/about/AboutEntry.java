/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.about;

import java.net.MalformedURLException;
import java.net.URL;

public class AboutEntry {

    public static class License {

        public static final License UNKNOWN = new License("Unknown", null, null);
        public static final License APL2;
        public static final License EPL;

        static {
            URL urlApache = null;
            URL urlEpl = null;
            try {
                urlApache = new URL("http://www.apache.org/licenses/");
                urlEpl = new URL("https://www.eclipse.org/legal/epl-2.0/");
            } catch (MalformedURLException e) {
            }

            APL2 = new License("Apache License 2.0", null, urlApache);
            EPL = new License("EPL", "Eclipse Public License", urlEpl);
        }

        private final String name;
        private final String text;
        private final URL url;

        public License(final String name, final String text, final URL url) {
            this.name = name;
            this.text = text;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getText() {
            return text;
        }

        public URL getUrl() {
            return url;
        }
    }

    private String id;

    private String name;
    private String version;
    private License license = License.UNKNOWN;
    private String notice;

    public void setId(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setLicense(final License license) {
        this.license = license != null ? license : License.UNKNOWN;
    }

    public License getLicense() {
        return license;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return notice;
    }
}
