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
 *     Arthur Deschamps - EPL license detection
 *******************************************************************************/
package org.eclipse.kapua.commons.about;

import com.google.common.io.CharStreams;
import org.eclipse.kapua.commons.about.AboutEntry.License;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class AboutScanner {

    private static final Logger LOG = LoggerFactory.getLogger(AboutScanner.class);

    private final List<AboutEntry> entries;

    private AboutScanner(final Stream<AboutEntry> entries) {
        this.entries = entries.sorted(Comparator.comparing(AboutEntry::getId)).collect(Collectors.toList());
    }

    public List<AboutEntry> getEntries() {
        return entries;
    }

    public static AboutScanner scan() {
        return scan(ClasspathHelper.forClassLoader().stream());
    }

    private static AboutEntry map(URL url) {

        AboutEntry result = null;

        try (InputStream in = url.openStream();
                ZipInputStream zis = new ZipInputStream(in)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ("META-INF/MANIFEST.MF".equals(entry.getName())) {
                    result = processOsgiManifest(result, entry, zis);
                    result = processPlainManifest(result, url, zis);
                } else if (entry.getName().startsWith("META-INF/maven/") && entry.getName().endsWith("/pom.properties")) {
                    result = processMavenProperties(result, entry, zis);
                } else if ("META-INF/NOTICE.txt".equals(entry.getName())) {
                    result = processNotice(result, url, zis);
                } else if ("META-INF/NOTICE".equals(entry.getName())) {
                    result = processNotice(result, url, zis);
                } else if ("about.html".equals(entry.getName())) {
                    result = processHtmlFile(result, url, zis);
                } else if ("META-INF/LICENSE.txt".equals(entry.getName())) {
                    result = processLicense(result, url, zis);
                } else if ("META-INF/LICENSE".equals(entry.getName())) {
                    result = processLicense(result, url, zis);
                }
            }
        } catch (IOException e) {
            // ignore
        }

        return result;
    }

    private static AboutEntry processPlainManifest(AboutEntry about, final URL url, final InputStream in) {
        try {
            final Manifest mf = new Manifest(in);
            final String name = mf.getMainAttributes().getValue("Specification-Title");
            final String version = mf.getMainAttributes().getValue("Specification-Version");

            if (name == null || version == null) {
                return about;
            }

            about = needAbout(about);
            setIdFromUrl(about, url);

            if (about.getName() == null && about.getVersion() == null) {
                about.setName(name);
                about.setVersion(version);
            }

        } catch (Exception e) {
            LOG.debug("Problem during archive processing:", e);
        }
        return about;
    }

    /**
     * Detect if the underlying project is EPL licensed
     */
    private static AboutEntry processHtmlFile(AboutEntry about, final URL url, final InputStream in) {

        about = processNotice(about, url, in);

        final List<String> keywords = Arrays.asList("epl", "eclipse public license");
        final String fileContent = about.getNotice();

        // Makes sure all the keywords are contained in the about.html file
        if (keywords.stream().allMatch(keyword -> fileContent.toLowerCase().contains(keyword.toLowerCase()))) {
            about.setLicense(License.EPL);
        } else {
            // Default license
            about.setLicense(License.UNKNOWN);
        }

        return about;
    }

    /**
     * Parse an Apache NOTICE file
     */
    private static AboutEntry processNotice(AboutEntry about, final URL url, final InputStream in) {

        about = needAbout(about);

        setIdFromUrl(about, url);

        try {
            about.setNotice(CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8)));
        } catch (Exception e) {
        }

        return about;
    }

    /**
     * Parse Apache LICENSE files
     */
    private static AboutEntry processLicense(AboutEntry about, final URL url, final InputStream in) {

        about = needAbout(about);

        setIdFromUrl(about, url);

        try {
            final String text = CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8));
            if (about.getLicense() == License.UNKNOWN) {
                // only set the text of we don't have any more precise information
                about.setLicense(new License(null, text, null));
            }
        } catch (Exception e) {
        }

        return about;
    }

    private static void setIdFromUrl(AboutEntry about, final URL url) {
        if (about.getId() == null) {
            final String[] paths = url.getPath().split("/");
            if (paths.length < 1) {
                // set the full URL, we don't understand it
                about.setId("url:" + url.toString());
            } else {
                // set URL based id
                about.setId("url:" + paths[paths.length - 1]);
            }
        }
    }

    private static AboutEntry processMavenProperties(AboutEntry about, final ZipEntry entry, final InputStream in) {
        try {
            final Properties p = new Properties();
            p.load(in);

            final String groupId = p.getProperty("groupId");
            final String artifactId = p.getProperty("artifactId");
            final String version = p.getProperty("version");

            if (groupId != null && artifactId != null && version != null) {

                about = needAbout(about);

                // check if an ID is already present, maven goes last
                if (about.getId() == null || about.getId().startsWith("url:")) {
                    about.setId(String.format("mvn:%s:%s:%s", groupId, artifactId, version));
                }

                if (about.getVersion() == null) {
                    about.setVersion(version);
                }
                if (about.getName() == null) {
                    about.setName(artifactId);
                }
            }
        } catch (Exception e) {
        }
        return about;
    }

    private static AboutEntry processOsgiManifest(AboutEntry about, final ZipEntry entry, final InputStream in) {
        try {
            final Manifest mf = new Manifest(in);
            final String mfv = mf.getMainAttributes().getValue("Bundle-ManifestVersion");
            if (!"2".equals(mfv)) {
                return about;
            }

            // OSGi

            final String bsn = mf.getMainAttributes().getValue("Bundle-SymbolicName");
            final String version = mf.getMainAttributes().getValue("Bundle-Version");
            final String name = mf.getMainAttributes().getValue("Bundle-Name");
            final String licenseString = mf.getMainAttributes().getValue("Bundle-License");

            about = needAbout(about);

            about.setId(String.format("osgi:%s:%s", bsn, version));
            about.setName(name);
            about.setVersion(version);
            about.setLicense(parseOsgiLicense(licenseString));

        } catch (Exception e) {
        }
        return about;
    }

    private static License parseOsgiLicense(final String string) {
        if (string == null || string.isEmpty()) {
            return License.UNKNOWN;
        }

        if (string.equals(License.APL2.getUrl().toString())) {
            return License.APL2;
        }

        try {
            URL url = new URL(string);
            return new License(null, null, url);
        } catch (Exception e) {
        }

        return License.UNKNOWN;
    }

    private static AboutEntry needAbout(AboutEntry about) {
        if (about != null) {
            return about;
        } else {
            return new AboutEntry();
        }
    }

    public static AboutScanner scan(final Stream<URL> urls) {
        return new AboutScanner(urls.map(AboutScanner::map).filter(entry -> entry != null));
    }
}
