/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundleWithLookup
{

    Resources INSTANCE = GWT.create(Resources.class);

    @Source("icons/16x16/icon.png")
    ImageResource serverClusterUpgrade16();

    @Source("icons/32x32/icon.png")
    ImageResource serverClusterUpgrade32();

    @Source("icons/16x16/icon.png")
    ImageResource serverCluster16();

    @Source("icons/32x32/icon.png")
    ImageResource serverCluster32();

    @Source("icons/16x16/icon.png")
    ImageResource browserFirefox16();

    @Source("icons/32x32/icon.png")
    ImageResource browserFirefox32();

    @Source("icons/16x16/icon.png")
    ImageResource browserSafari16();

    @Source("icons/32x32/icon.png")
    ImageResource browserSafari32();

    @Source("icons/16x16/icon.png")
    ImageResource browserIE16();

    @Source("icons/32x32/icon.png")
    ImageResource browserIE32();

    @Source("icons/16x16/icon.png")
    ImageResource browserChrome16();

    @Source("icons/16x16/icon.png")
    ImageResource flagUnitedKingdom16();

    @Source("icons/32x32/icon.png")
    ImageResource flagUnitedKingdom32();

    @Source("icons/16x16/icon.png")
    ImageResource flagItaly16();

    @Source("icons/32x32/icon.png")
    ImageResource flagItaly32();

    @Source("icons/16x16/icon.png")
    ImageResource locate16();

    @Source("icons/32x32/icon.png")
    ImageResource locate32();

    @Source("icons/16x16/icon.png")
    ImageResource switchToAccount16();

    @Source("icons/16x16/icon.png")
    ImageResource server16();

    @Source("icons/32x32/icon.png")
    ImageResource switchToAccount32();

    @Source("icons/16x16/icon.png")
    ImageResource userLogout16();

    @Source("icons/32x32/icon.png")
    ImageResource userLogout32();

    @Source("icons/16x16/icon.png")
    ImageResource userEdit16();

    @Source("icons/32x32/icon.png")
    ImageResource userEdit32();

    @Source("icons/32x32/icon.png")
    ImageResource instantActivationBarCode();

    @Source("icons/16x16/icon.png")
    ImageResource wrench();

    @Source("icons/16x16/icon.png")
    ImageResource magnifier16();

    @Source("icons/32x32/icon.png")
    ImageResource magnifier32();

    @Source("icons/16x16/icon.png")
    ImageResource provisioning16();

    @Source("icons/32x32/icon.png")
    ImageResource provisioning32();

    @Source("icons/16x16/icon.png")
    ImageResource filterDelete16();

    @Source("icons/16x16/icon.png")
    ImageResource downloadMac16();

    @Source("icons/16x16/icon.png")
    ImageResource downloadLinux16();

    @Source("icons/16x16/icon.png")
    ImageResource downloadWindows16();

    @Source("icons/16x16/icon.png")
    ImageResource keyGo();

    @Source("icons/16x16/icon.png")
    ImageResource keyRemove();

    @Source("icons/16x16/icon.png")
    ImageResource serverKey();

    @Source("icons/16x16/icon.png")
    ImageResource computerKey();

    @Source("icons/16x16/icon.png")
    ImageResource cross16();

    @Source("icons/16x16/icon.png")
    ImageResource greenTick16();

    @Source("icons/16x16/icon.png")
    ImageResource yellowTick16();

    @Source("icons/16x16/icon.png")
    ImageResource selectAllText16();

    @Source("icons/16x16/icon.png")
    ImageResource jobLog16();

    @Source("icons/16x16/icon.png")
    ImageResource play16();

    @Source("icons/32x32/icon.png")
    ImageResource provisionRequest32();

    @Source("icons/16x16/icon.png")
    ImageResource provisionRequest16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptGear16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptPlayGrey16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptPlay16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptError16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptGo16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptTic16();

    @Source("icons/16x16/icon.png")
    ImageResource scriptDelete16();

    @Source("icons/32x32/icon.png")
    ImageResource batch32();

    @Source("icons/16x16/icon.png")
    ImageResource batch16();

    @Source("icons/32x32/icon.png")
    ImageResource yellowBullet14();

    @Source("icons/32x32/icon.png")
    ImageResource redBullet14();

    @Source("icons/32x32/icon.png")
    ImageResource greenBullet14();

    @Source("icons/32x32/icon.png")
    ImageResource blackBullet14();

    @Source("icons/32x32/icon.png")
    ImageResource greenAndBlackBullet14();

    @Source("icons/32x32/icon.png")
    ImageResource apple();

    @Source("icons/32x32/icon.png")
    ImageResource android();

    @Source("icons/32x32/icon.png")
    ImageResource blank();

    @Source("icons/16x16/icon.png")
    ImageResource toDoListChecked();

    @Source("icons/16x16/icon.png")
    ImageResource disabledButConnected16();

    @Source("icons/32x32/icon.png")
    ImageResource disabledButConnected32();

    @Source("icons/16x16/icon.png")
    ImageResource black16();

    @Source("icons/32x32/icon.png")
    ImageResource black32();

    @Source("icons/16x16/icon.png")
    ImageResource cloudSettings16();

    @Source("icons/32x32/icon.png")
    ImageResource cloudSettings32();

    @Source("icons/16x16/icon.png")
    ImageResource cloudBoxSettings16();

    @Source("icons/32x32/icon.png")
    ImageResource cloudBoxSettings32();

    @Source("icons/16x16/icon.png")
    ImageResource administrator();

    @Source("icons/16x16/icon.png")
    ImageResource childAccounts16();

    @Source("icons/32x32/icon.png")
    ImageResource childAccounts32();

    @Source("icons/32x32/icon.png")
    ImageResource usages32();

    @Source("icons/16x16/icon.png")
    ImageResource usages16();

    @Source("icons/16x16/icon.png")
    ImageResource rules16();

    @Source("icons/32x32/icon.png")
    ImageResource rules32();

    @Source("icons/32x32/icon.png")
    ImageResource wheel();

    @Source("icons/32x32/icon.png")
    ImageResource dashboard32();

    @Source("icons/16x16/icon.png")
    ImageResource dashboard();

    @Source("icons/32x32/icon.png")
    ImageResource alerts32();

    @Source("icons/16x16/icon.png")
    ImageResource alerts();

    @Source("icons/32x32/icon.png")
    ImageResource devices32();

    @Source("icons/16x16/icon.png")
    ImageResource devices();

    @Source("icons/32x32/icon.png")
    ImageResource data32();

    @Source("icons/16x16/icon.png")
    ImageResource data();

    @Source("icons/32x32/icon.png")
    ImageResource dataByTopic32();

    @Source("icons/16x16/icon.png")
    ImageResource dataByTopic();

    @Source("icons/32x32/icon.png")
    ImageResource dataByAsset32();

    @Source("icons/16x16/icon.png")
    ImageResource dataByAsset();

    @Source("icons/32x32/icon.png")
    ImageResource audit32();

    @Source("icons/16x16/icon.png")
    ImageResource audit();

    @Source("icons/32x32/icon.png")
    ImageResource settings32();

    @Source("icons/16x16/icon.png")
    ImageResource settings();

    @Source("icons/16x16/icon.png")
    ImageResource healthCheck16();

    @Source("icons/32x32/icon.png")
    ImageResource healthCheck32();

    @Source("icons/32x32/icon.png")
    ImageResource update32();

    @Source("icons/16x16/icon.png")
    ImageResource update();

    @Source("icons/16x16/icon.png")
    ImageResource add();

    @Source("icons/16x16/icon.png")
    ImageResource edit();

    @Source("icons/16x16/icon.png")
    ImageResource delete();

    @Source("icons/16x16/icon.png")
    ImageResource delete16();

    @Source("icons/16x16/icon.png")
    ImageResource download();

    @Source("icons/16x16/icon.png")
    ImageResource exportExcel();

    @Source("icons/16x16/icon.png")
    ImageResource exportCSV();

    @Source("icons/16x16/icon.png")
    ImageResource live();

    @Source("icons/16x16/icon.png")
    ImageResource deviceProfile();

    @Source("icons/16x16/icon.png")
    ImageResource history();

    @Source("icons/16x16/icon.png")
    ImageResource info16();

    @Source("icons/32x32/icon.png")
    ImageResource info32();

    @Source("icons/32x32/icon.png")
    ImageResource warn32();

    @Source("icons/16x16/icon.png")
    ImageResource warn16();

    @Source("icons/32x32/icon.png")
    ImageResource error32();

    @Source("icons/16x16/icon.png")
    ImageResource error16();

    @Source("icons/16x16/icon.png")
    ImageResource chart();

    @Source("icons/16x16/icon.png")
    ImageResource chartEdit();

    @Source("icons/16x16/icon.png")
    ImageResource table();

    @Source("icons/32x32/icon.png")
    ImageResource table32();

    @Source("icons/32x32/icon.png")
    ImageResource users32();

    @Source("icons/16x16/icon.png")
    ImageResource users16();

    @Source("icons/16x16/icon.png")
    ImageResource sim16();

    @Source("icons/16x16/icon.png")
    ImageResource simGroups16();

    @Source("icons/16x16/icon.png")
    ImageResource simprovider16();

    @Source("icons/32x32/icon.png")
    ImageResource simprovider32();

    @Source("icons/16x16/icon.png")
    ImageResource pki16();

    @Source("icons/32x32/icon.png")
    ImageResource pki32();

    @Source("icons/16x16/icon.png")
    ImageResource pki16Refuse();

    @Source("icons/32x32/icon.png")
    ImageResource pki32Refuse();

    @Source("icons/16x16/icon.png")
    ImageResource pki16Accept();

    @Source("icons/32x32/icon.png")
    ImageResource pki32Accept();

    @Source("icons/16x16/icon.png")
    ImageResource simOperations();

    @Source("icons/16x16/icon.png")
    ImageResource wakeupSMS();

    @Source("icons/16x16/icon.png")
    ImageResource rebootSMS();

    @Source("icons/16x16/icon.png")
    ImageResource enabled16();

    @Source("icons/16x16/icon.png")
    ImageResource bad16();

    @Source("icons/16x16/icon.png")
    ImageResource disabled16();

    @Source("icons/32x32/icon.png")
    ImageResource enabled32();

    @Source("icons/32x32/icon.png")
    ImageResource bad32();

    @Source("icons/32x32/icon.png")
    ImageResource disabled32();

    @Source("icons/16x16/icon.png")
    ImageResource alwaysPassed16();

    @Source("icons/16x16/icon.png")
    ImageResource sometimesFailed16();

    @Source("icons/16x16/icon.png")
    ImageResource alwaysFailed16();

    @Source("icons/16x16/icon.png")
    ImageResource noData16();

    @Source("icons/16x16/icon.png")
    ImageResource calendar();

    @Source("icons/16x16/icon.png")
    ImageResource deviceMap();

    @Source("icons/16x16/icon.png")
    ImageResource email();

    @Source("icons/32x32/icon.png")
    ImageResource email32();

    @Source("icons/16x16/icon.png")
    ImageResource phone();

    @Source("icons/16x16/icon.png")
    ImageResource reprovision();

    @Source("icons/16x16/icon.png")
    ImageResource sync();

    @Source("icons/32x32/icon.png")
    ImageResource phone32();

    @Source("icons/16x16/icon.png")
    ImageResource twilio();

    @Source("icons/32x32/icon.png")
    ImageResource twilio32();

    @Source("icons/16x16/icon.png")
    ImageResource twitter();

    @Source("icons/32x32/icon.png")
    ImageResource twitter32();

    @Source("icons/32x32/icon.png")
    ImageResource mqtt();

    @Source("icons/32x32/icon.png")
    ImageResource mqtt32();

    @Source("icons/16x16/icon.png")
    ImageResource rest();

    @Source("icons/32x32/icon.png")
    ImageResource rest32();

    @Source("icons/16x16/icon.png")
    ImageResource alert();

    @Source("icons/32x32/icon.png")
    ImageResource alert32();

    @Source("icons/16x16/icon.png")
    ImageResource accounts();

    @Source("icons/32x32/icon.png")
    ImageResource accounts32();

    @Source("icons/16x16/icon.png")
    ImageResource tasks();

    @Source("icons/32x32/icon.png")
    ImageResource tasks32();

    @Source("icons/16x16/icon.png")
    ImageResource change_password();

    @Source("icons/32x32/icon.png")
    ImageResource change_password32();

    @Source("icons/16x16/icon.png")
    ImageResource ssl_certificates();

    @Source("icons/32x32/icon.png")
    ImageResource ssl_certificates32();

    @Source("icons/16x16/icon.png")
    ImageResource mail_server_setting();

    @Source("icons/32x32/icon.png")
    ImageResource mail_server_setting32();

    @Source("icons/16x16/icon.png")
    ImageResource bluetooth();

    @Source("icons/16x16/icon.png")
    ImageResource monitorDenali();

    @Source("icons/16x16/icon.png")
    ImageResource redo();

    @Source("icons/16x16/icon.png")
    ImageResource refresh();

    @Source("icons/16x16/icon.png")
    ImageResource provision();

    @Source("icons/16x16/icon.png")
    ImageResource configuration();

    @Source("icons/32x32/icon.png")
    ImageResource playEnabled();

    @Source("icons/32x32/icon.png")
    ImageResource playDisabled();

    @Source("icons/16x16/icon.png")
    ImageResource terminal();

    @Source("icons/16x16/icon.png")
    ImageResource package16();

    @Source("icons/16x16/icon.png")
    ImageResource packageGreen16();

    @Source("icons/16x16/icon.png")
    ImageResource packageGo16();

    @Source("icons/16x16/icon.png")
    ImageResource packageAdd();

    @Source("icons/16x16/icon.png")
    ImageResource packageDelete();

    @Source("icons/16x16/icon.png")
    ImageResource plugin();

    @Source("icons/16x16/icon.png")
    ImageResource disconnect();

    @Source("icons/16x16/icon.png")
    ImageResource cloud16();

    @Source("icons/16x16/icon.png")
    ImageResource clock16();

    @Source("icons/16x16/icon.png")
    ImageResource gps16();

    @Source("icons/16x16/icon.png")
    ImageResource dog16();

    @Source("icons/16x16/icon.png")
    ImageResource plugin16();

    @Source("icons/16x16/icon.png")
    ImageResource component();

    @Source("icons/16x16/icon.png")
    ImageResource network();

    @Source("icons/16x16/icon.png")
    ImageResource accept();

    @Source("icons/16x16/icon.png")
    ImageResource accept16();

    @Source("icons/32x32/icon.png")
    ImageResource accept32();

    @Source("icons/16x16/icon.png")
    ImageResource cancel16();

    @Source("icons/16x16/icon.png")
    ImageResource settingsUpload();

    @Source("icons/32x32/icon.png")
    ImageResource settingsUpload32();

    @Source("icons/16x16/icon.png")
    ImageResource settingsDownload();

    @Source("icons/32x32/icon.png")
    ImageResource settingsDownload32();

    @Source("icons/16x16/icon.png")
    ImageResource snapshots();

    @Source("icons/16x16/icon.png")
    ImageResource snapshotUpload();

    @Source("icons/16x16/icon.png")
    ImageResource snapshotRollback();

    @Source("icons/16x16/icon.png")
    ImageResource snapshotDownload();

    @Source("icons/16x16/icon.png")
    ImageResource bundles();

    @Source("icons/16x16/icon.png")
    ImageResource bundleStart();

    @Source("icons/16x16/icon.png")
    ImageResource bundleStop();

    @Source("icons/16x16/icon.png")
    ImageResource databaseConnect();

    @Source("icons/16x16/icon.png")
    ImageResource lock16();

    @Source("icons/16x16/icon.png")
    ImageResource lockGreen16();

    @Source("icons/16x16/icon.png")
    ImageResource unlock16();

    // DM Device Management Icons
    @Source("icons/16x16/icon.png")
    ImageResource dmLock16();

    @Source("icons/16x16/icon.png")
    ImageResource dmUnlock16();

    @Source("icons/16x16/icon.png")
    ImageResource dmLockBreak16();

    @Source("icons/16x16/icon.png")
    ImageResource dmLockBreakRed16();

    @Source("icons/16x16/icon.png")
    ImageResource help16();

    @Source("icons/32x32/icon.png")
    ImageResource help32();

    @Source("icons/32x32/icon.png")
    ImageResource vpn();

    @Source("icons/32x32/icon.png")
    ImageResource vpn32();

    @Source("icons/32x32/icon.png")
    ImageResource ifttt();

    @Source("icons/32x32/icon.png")
    ImageResource ifttt32();

    @Source("icons/16x16/icon.png")
    ImageResource smallBusiness();

    @Source("icons/32x32/icon.png")
    ImageResource smallBusiness32();

    @Source("icons/32x32/icon.png")
    ImageResource diagnostics();

    @Source("icons/32x32/icon.png")
    ImageResource diagnostics32();

    @Source("html/documentation.html")
    TextResource documentationHtml();

    @Source("html/devices.html")
    TextResource devicesHtml();

}
