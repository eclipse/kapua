/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringWriter;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;


@Category(JUnitTests.class)
public class KapuaMessageTest {

    private static final String KAPUA_MESSAGE_XML_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<message>\n" +
            "   <id>11111111-2222-3333-4444-555555555555</id>\n" +
            "   <scopeId>AQ</scopeId>\n" +
            "   <deviceId>AQ</deviceId>\n" +
            "   <receivedOn>2017-01-18T12:11:46.000Z</receivedOn>\n" +
            "   <sentOn>2017-01-18T12:10:56.000Z</sentOn>\n" +
            "   <capturedOn>2017-01-18T12:10:46.000Z</capturedOn>\n" +
            "   <position>\n" +
            "      <altitude>430.3</altitude>\n" +
            "      <heading>280.0</heading>\n" +
            "      <latitude>15.3333</latitude>\n" +
            "      <longitude>45.1111</longitude>\n" +
            "      <precision>12.0</precision>\n" +
            "      <satellites>5</satellites>\n" +
            "      <speed>60.2</speed>\n" +
            "      <status>4</status>\n" +
            "      <timestamp>2017-01-18T12:10:46.000Z</timestamp>\n" +
            "   </position>\n" +
            "   <channel>\n" +
            "      <semanticParts>part1</semanticParts>\n" +
            "      <semanticParts>part2</semanticParts>\n" +
            "      <semanticParts>part3</semanticParts>\n" +
            "   </channel>\n" +
            "   <payload>\n" +
            "      <metrics>\n" +
            "         <metric>\n" +
            "            <valueType>string</valueType>\n" +
            "            <value>value1</value>\n" +
            "            <name>key1</name>\n" +
            "         </metric>\n" +
            "         <metric>\n" +
            "            <valueType>string</valueType>\n" +
            "            <value>value2</value>\n" +
            "            <name>key2</name>\n" +
            "         </metric>\n" +
            "      </metrics>\n" +
            "      <body>Ym9keQ==</body>\n" +
            "   </payload>\n" +
            "</message>\n";

    private ZonedDateTime referenceDate = ZonedDateTime.of(2017, 1, 18, 12, 10, 46, 0, ZoneId.of("UTC"));

    private Date capturedDate = Date.from(referenceDate.toInstant());

    private Date sentDate = Date.from(referenceDate.plusSeconds(10).toInstant());

    private Date receivedDate = Date.from(referenceDate.plusMinutes(1).toInstant());

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageTestJAXBContextProvider());
    }

    @Test
    public void kapuaMessage() {
        KapuaChannel kapuaChannel = new KapuaChannelImpl();
        KapuaPayload kapuaMetrics = new KapuaPayloadImpl();

        KapuaMessageUtil.populateChannel(kapuaChannel);
        KapuaMessageUtil.populatePayload(kapuaMetrics);
        KapuaMessage<?, ?> kapuaMessage = new KapuaMessageImpl<>(kapuaChannel, kapuaMetrics);
        KapuaMessageUtil.populateKapuaMessage(kapuaMessage, referenceDate);

        Assert.assertEquals(UUID.fromString("11111111-2222-3333-4444-555555555555"), kapuaMessage.getId());
        Assert.assertEquals(new KapuaEid(BigInteger.ONE), kapuaMessage.getScopeId());
        Assert.assertEquals(new KapuaEid(BigInteger.ONE), kapuaMessage.getDeviceId());
        Assert.assertEquals(receivedDate, kapuaMessage.getReceivedOn());
        Assert.assertEquals(sentDate, kapuaMessage.getSentOn());
        Assert.assertEquals(capturedDate, kapuaMessage.getCapturedOn());
        KapuaPosition position = new KapuaPositionImpl();
        KapuaMessageUtil.populatePosition(position, referenceDate);
        Assert.assertEquals(position.toDisplayString(), kapuaMessage.getPosition().toDisplayString());
        Assert.assertEquals(kapuaChannel, kapuaMessage.getChannel());
        Assert.assertEquals(kapuaMetrics, kapuaMessage.getPayload());
    }

    @Test
    public void setMessageChannelAndPayload() throws Exception {
        KapuaChannel kapuaChannel = new KapuaChannelImpl();
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();

        KapuaMessageUtil.populateChannel(kapuaChannel);
        KapuaMessageUtil.populatePayload(kapuaPayload);
        KapuaMessage<KapuaChannel, KapuaPayload> kapuaMessage = new KapuaMessageImpl<>();
        kapuaMessage.setChannel(kapuaChannel);
        kapuaMessage.setPayload(kapuaPayload);

        Assert.assertEquals(kapuaChannel, kapuaMessage.getChannel());
        Assert.assertEquals(kapuaPayload, kapuaMessage.getPayload());
    }

    @Test
    public void messageEquals() throws Exception {
        KapuaMessage<KapuaChannel, KapuaPayload> kapuaMessageFirst = new KapuaMessageImpl<>();
        KapuaMessageUtil.populateKapuaMessage(kapuaMessageFirst, referenceDate);
        KapuaMessage<KapuaChannel, KapuaPayload> kapuaMessageSecond = new KapuaMessageImpl<>();
        KapuaMessageUtil.populateKapuaMessage(kapuaMessageSecond, referenceDate);

        Assert.assertEquals(0, ((KapuaMessageImpl<KapuaChannel, KapuaPayload>) kapuaMessageFirst).
                compareTo((KapuaMessageImpl<KapuaChannel, KapuaPayload>) kapuaMessageSecond));
    }

    @Test
    public void marshallMessage() throws Exception {
        KapuaChannel kapuaChannel = new KapuaChannelImpl();
        KapuaPayload kapuaMetrics = new KapuaPayloadImpl();

        KapuaMessageUtil.populateChannel(kapuaChannel);
        KapuaMessageUtil.populatePayload(kapuaMetrics);
        KapuaMessage<KapuaChannel, KapuaPayload> kapuaMessage = new KapuaMessageImpl<>(kapuaChannel, kapuaMetrics);
        KapuaMessageUtil.populateKapuaMessage(kapuaMessage, referenceDate);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(kapuaMessage, strWriter);
        Assert.assertEquals(KAPUA_MESSAGE_XML_STR, strWriter.toString());
    }

}
