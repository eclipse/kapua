/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.about;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


@Category(JUnitTests.class)
public class AboutEntryTest {

    private String[] names;
    private String[] texts;
    private URL[] urls;
    private ArrayList<AboutEntry.License> licenseList;

    @Before
    public void initialize() throws MalformedURLException {
        names = new String[]{"Name", null};
        texts = new String[]{"Text", null};
        String validSpec = "https://www.google.com";
        urls = new URL[]{new URL(validSpec), null};
        licenseList = new ArrayList<>();
    }

    @Test
    public void aboutEntryLicenseTest() {
        for (String name : names) {
            for (String text : texts) {
                for (URL url : urls) {
                    AboutEntry.License license = new AboutEntry.License(name, text, url);
                    Assert.assertEquals("Expected and actual values should be the same.", name, license.getName());
                    Assert.assertEquals("Expected and actual values should be the same.", text, license.getText());
                    Assert.assertEquals("Expected and actual values should be the same.", url, license.getUrl());
                }
            }
        }
    }

    @Test
    public void licenseGetNameTest() {
        for (String name : names) {
            for (String text : texts) {
                for (URL url : urls) {
                    AboutEntry.License license = new AboutEntry.License(name, text, url);
                    licenseList.add(license);
                }
            }
        }
        for (int i = 0; i < licenseList.size() / 2; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", "Name", licenseList.get(i).getName());
        }
        for (int i = licenseList.size() / 2; i < licenseList.size(); i++) {
            Assert.assertNull("Null expected.", licenseList.get(i).getName());
        }
    }

    @Test
    public void licenseGetTextTest() {
        for (String text : texts) {
            for (String name : names) {
                for (URL url : urls) {
                    AboutEntry.License license = new AboutEntry.License(name, text, url);
                    licenseList.add(license);
                }
            }
        }
        for (int i = 0; i < licenseList.size() / 2; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", "Text", licenseList.get(i).getText());
        }
        for (int i = licenseList.size() / 2; i < licenseList.size(); i++) {
            Assert.assertNull("Null expected.", licenseList.get(i).getText());
        }
    }

    @Test
    public void licenseGetUrlTest() {
        for (URL url : urls) {
            for (String name : names) {
                for (String text : texts) {
                    AboutEntry.License license = new AboutEntry.License(name, text, url);
                    licenseList.add(license);
                }
            }
        }
        for (int i = 0; i < licenseList.size() / 2; i++) {
            Assert.assertEquals("Expected and actual values should be the same.", urls[0], licenseList.get(i).getUrl());
        }
        for (int i = licenseList.size() / 2; i < licenseList.size(); i++) {
            Assert.assertNull("Null expected.", licenseList.get(i).getUrl());
        }
    }

    @Test
    public void setAndGetIdTest() {
        String id = "valid_id";
        AboutEntry aboutEntry = new AboutEntry();
        Assert.assertNull("Null expected.", aboutEntry.getId());
        aboutEntry.setId(id);
        Assert.assertEquals("Expected and actual values should be the same.", id, aboutEntry.getId());
        aboutEntry.setId(null);
        Assert.assertNull("Null expected.", aboutEntry.getId());
    }

    @Test
    public void setAndGetNameTest() {
        String name = "valid_name";
        AboutEntry aboutEntry = new AboutEntry();
        Assert.assertNull("Null expected.", aboutEntry.getName());
        aboutEntry.setName(name);
        Assert.assertEquals("Expected and actual values should be the same.", name, aboutEntry.getName());
        aboutEntry.setName(null);
        Assert.assertNull("Null expected.", aboutEntry.getName());
    }

    @Test
    public void setAndGetVersionTest() {
        String version = "valid_version";
        AboutEntry aboutEntry = new AboutEntry();
        Assert.assertNull("Null expected.", aboutEntry.getVersion());
        aboutEntry.setVersion(version);
        Assert.assertEquals("Expected and actual values should be the same.", version, aboutEntry.getVersion());
        aboutEntry.setVersion(null);
        Assert.assertNull("Null expected.", aboutEntry.getVersion());
    }

    @Test
    public void setAndGetLicenseTest() throws MalformedURLException {
        String[] names = {"Name", null};
        String[] texts = {"Text", null};
        String validSpec = "https://www.google.com";
        URL[] urls = {new URL(validSpec), null};
        AboutEntry aboutEntry = new AboutEntry();

        for (String name : names) {
            for (String text : texts) {
                for (URL url : urls) {
                    AboutEntry.License license = new AboutEntry.License(name, text, url);
                    Assert.assertEquals("Unknown License expected", "Unknown", aboutEntry.getLicense().getName());
                    aboutEntry.setLicense(license);
                    Assert.assertEquals("Expected and actual values should be the same.", license, aboutEntry.getLicense());
                    aboutEntry.setLicense(null);
                    Assert.assertEquals("Unknown License expected", "Unknown", aboutEntry.getLicense().getName());
                }
            }
        }
    }

    @Test
    public void setAndGetNoticeTest() {
        String notice = "valid_notice";
        AboutEntry aboutEntry = new AboutEntry();
        Assert.assertNull("Null expected.", aboutEntry.getNotice());
        aboutEntry.setNotice(notice);
        Assert.assertEquals("Expected and actual values should be the same.", notice, aboutEntry.getNotice());
        aboutEntry.setNotice(null);
        Assert.assertNull("Null expected.", aboutEntry.getNotice());
    }
} 
