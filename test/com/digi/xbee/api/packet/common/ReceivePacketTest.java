/**
 * Copyright (c) 2014 Digi International Inc.,
 * All rights not expressly granted are reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Digi International Inc. 11001 Bren Road East, Minnetonka, MN 55343
 * =======================================================================
 */
package com.digi.xbee.api.packet.common;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.packet.APIFrameType;

public class ReceivePacketTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	public ReceivePacketTest() {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.digi.xbee.api.packet.common.ReceivePacket#createPacket(byte[])}.
	 * 
	 * <p>A {@code NullPointerException} exception must be thrown when parsing a 
	 * {@code null} byte array.</p>
	 */
	@Test
	public final void testCreatePacketNullPayload() {
		// Setup the resources for the test.
		byte[] payload = null;
		exception.expect(NullPointerException.class);
		exception.expectMessage(is(equalTo("Receive packet payload cannot be null.")));
		
		// Call the method under test that should throw a NullPointerException.
		ReceivePacket.createPacket(payload);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.packet.common.ReceivePacket#createPacket(byte[])}.
	 * 
	 * <p>An {@code IllegalArgumentException} exception must be thrown when 
	 * parsing an empty byte array.</p>
	 */
	@Test
	public final void testCreatePacketEmptyPayload() {
		// Setup the resources for the test.
		byte[] payload = new byte[0];
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Incomplete Receive packet.")));
		
		// Call the method under test that should throw an IllegalArgumentException.
		ReceivePacket.createPacket(payload);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.packet.common.ReceivePacket#createPacket(byte[])}.
	 * 
	 * <p>An {@code IllegalArgumentException} exception must be thrown when 
	 * parsing a byte array shorter than the needed one is provided.</p>
	 */
	@Test
	public final void testCreatePacketPayloadShorterThanNeeded() {
		// Setup the resources for the test.
		int frameType = APIFrameType.RECEIVE_PACKET.getValue();
		XBee64BitAddress source64Addr = new XBee64BitAddress("0013A2004032D9AB");
		XBee16BitAddress source16Addr = new XBee16BitAddress("D817");
		
		byte[] payload = new byte[11];
		payload[0] = (byte)frameType;
		System.arraycopy(source64Addr.getValue(), 0, payload, 1, source64Addr.getValue().length);
		System.arraycopy(source64Addr.getValue(), 0, payload, 9, source16Addr.getValue().length);
		
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Incomplete Receive packet.")));
		
		// Call the method under test that should throw an IllegalArgumentException.
		ReceivePacket.createPacket(payload);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.packet.common.ReceivePacket#createPacket(byte[])}.
	 * 
	 * <p>An {@code IllegalArgumentException} exception must be thrown when 
	 * parsing a byte array not including the Frame type.</p>
	 */
	@Test
	public final void testCreatePacketPayloadNotIncludingFrameType() {
		// Setup the resources for the test.
		XBee64BitAddress source64Addr = new XBee64BitAddress("0013A2004032D9AB");
		XBee16BitAddress source16Addr = new XBee16BitAddress("D817");
		int options = 40;
		byte[] data = new byte[]{0x68, 0x6F, 0x6C, 0x61};
		
		byte[] payload = new byte[11 + data.length];
		System.arraycopy(source64Addr.getValue(), 0, payload, 0, source64Addr.getValue().length);
		System.arraycopy(source16Addr.getValue(), 0, payload, 8, source16Addr.getValue().length);
		payload[10] = (byte)options;
		System.arraycopy(data, 0, payload, 11, data.length);
		
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(is(equalTo("Payload is not a Receive packet.")));
		
		// Call the method under test that should throw an IllegalArgumentException.
		ReceivePacket.createPacket(payload);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.packet.common.ReceivePacket#createPacket(byte[])}.
	 * 
	 * <p>A valid API Receive packet with the provided options without RF data is 
	 * created.</p>
	 */
	@Test
	public final void testCreatePacketValidPayloadWithoutData() {
		// Setup the resources for the test.
		int frameType = APIFrameType.RECEIVE_PACKET.getValue();
		XBee64BitAddress source64Addr = new XBee64BitAddress("0013A2004032D9AB");
		XBee16BitAddress source16Addr = new XBee16BitAddress("D817");
		int options = 40;
		
		byte[] payload = new byte[12];
		payload[0] = (byte)frameType;
		System.arraycopy(source64Addr.getValue(), 0, payload, 1, source64Addr.getValue().length);
		System.arraycopy(source16Addr.getValue(), 0, payload, 9, source16Addr.getValue().length);
		payload[11] = (byte)options;
		
		// Call the method under test.
		ReceivePacket packet = ReceivePacket.createPacket(payload);
		
		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(payload.length)));
		assertThat("Returned source 64-bit address is not the expected one", packet.get64bitAddress(), is(equalTo(source64Addr)));
		assertThat("Returned source 16-bit address is not the expected one", packet.get16bitAddress(), is(equalTo(source16Addr)));
		assertThat("Returned received options is not the expected one", packet.getReceiveOptions(), is(equalTo(options)));
		assertThat("Returned Received Data is not the expected one", packet.getReceivedData(), is(nullValue()));
		
		assertThat("Returned payload array is not the expected one", packet.getPacketData(), is(equalTo(payload)));
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.packet.common.ReceivePacket#createPacket(byte[])}.
	 * 
	 * <p>A valid API Receive packet with the provided options and RF data is 
	 * created.</p>
	 */
	@Test
	public final void testCreatePacketValidPayloadWithData() {
		// Setup the resources for the test.
		int frameType = APIFrameType.RECEIVE_PACKET.getValue();
		XBee64BitAddress source64Addr = new XBee64BitAddress("0013A2004032D9AB");
		XBee16BitAddress source16Addr = new XBee16BitAddress("D817");
		int options = 40;
		byte[] data = new byte[]{0x68, 0x6F, 0x6C, 0x61};
		
		byte[] payload = new byte[12 + data.length];
		payload[0] = (byte)frameType;
		System.arraycopy(source64Addr.getValue(), 0, payload, 1, source64Addr.getValue().length);
		System.arraycopy(source16Addr.getValue(), 0, payload, 9, source16Addr.getValue().length);
		payload[11] = (byte)options;
		System.arraycopy(data, 0, payload, 12, data.length);
		
		// Call the method under test.
		ReceivePacket packet = ReceivePacket.createPacket(payload);
		
		// Verify the result.
		assertThat("Returned length is not the expected one", packet.getPacketLength(), is(equalTo(payload.length)));
		assertThat("Returned source 64-bit address is not the expected one", packet.get64bitAddress(), is(equalTo(source64Addr)));
		assertThat("Returned source 16-bit address is not the expected one", packet.get16bitAddress(), is(equalTo(source16Addr)));
		assertThat("Returned received options is not the expected one", packet.getReceiveOptions(), is(equalTo(options)));
		assertThat("Returned Received Data is not the expected one", packet.getReceivedData(), is(equalTo(data)));
		
		assertThat("Returned payload array is not the expected one", packet.getPacketData(), is(equalTo(payload)));
	}
}