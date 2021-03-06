/**
 * Copyright (c) 2014-2016 Digi International Inc.,
 * All rights not expressly granted are reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Digi International Inc. 11001 Bren Road East, Minnetonka, MN 55343
 * =======================================================================
 */
package com.digi.xbee.api;

import java.io.IOException;

import com.digi.xbee.api.connection.serial.SerialPortJavacomm;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.digi.xbee.api.exceptions.ATCommandException;
import com.digi.xbee.api.exceptions.InterfaceNotOpenException;
import com.digi.xbee.api.exceptions.InvalidOperatingModeException;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.ATCommand;
import com.digi.xbee.api.models.ATCommandResponse;
import com.digi.xbee.api.models.ATCommandStatus;
import com.digi.xbee.api.models.OperatingMode;

public class SetParameterTest {

	// Constants
	private static final String PARAM_INVALID = "N";
	private static final String PARAM_NI = "NI";
	private static final String VALUE_NI = "Yoda";
	
	// Variables.
	private SerialPortJavacomm mockedPort;
	private XBeeDevice xbeeDevice;
	
	private ATCommandResponse mocketATCommandResponse;
	
	@Before
	public void setup() {
		// Mock an RxTx IConnectionInterface.
		mockedPort = Mockito.mock(SerialPortJavacomm.class);
		Mockito.when(mockedPort.isOpen()).thenReturn(true);
		
		// Instantiate an XBeeDevice object with basic parameters.
		xbeeDevice = PowerMockito.spy(new XBeeDevice(mockedPort));
		Mockito.when(xbeeDevice.getOperatingMode()).thenReturn(OperatingMode.API);
		
		// Mock an ATCommandResponse.
		mocketATCommandResponse = Mockito.mock(ATCommandResponse.class);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that if the parameter is null, a {@code NullPointerException} is thrown when 
	 * setting a parameter.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=NullPointerException.class)
	public void testSetParameterNullParameter() throws TimeoutException, XBeeException {
		xbeeDevice.setParameter(null, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that if the parameter is not valid, an {@code IllegalArgumentException} is thrown 
	 * when setting a parameter.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSetParameterInvalidParameter() throws TimeoutException, XBeeException {
		xbeeDevice.setParameter(PARAM_INVALID, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that if the parameter value is null, a {@code NullPointerException} is thrown 
	 * when setting a parameter.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=NullPointerException.class)
	public void testSetParameterNullParameterValue() throws TimeoutException, XBeeException {
		xbeeDevice.setParameter(PARAM_NI, null);
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code InterfaceNotOpenException} is thrown when trying to set a parameter 
	 * with the connection closed.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=InterfaceNotOpenException.class)
	public void testSetParameterConnectionClosed() throws TimeoutException, XBeeException {
		// Configure the connection to indicate it is closed when asked.
		Mockito.when(mockedPort.isOpen()).thenReturn(false);
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code InvalidOperatingModeException} is thrown when trying to set a parameter 
	 * and the operating mode of the device is UNKNOWN.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=InvalidOperatingModeException.class)
	public void testSetParameterInvalidOperatingMode() throws TimeoutException, XBeeException {
		// Configure the operating mode of the device to return UNKNOWN when asked.
		Mockito.when(xbeeDevice.getOperatingMode()).thenReturn(OperatingMode.UNKNOWN);
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code InvalidOperatingModeException} is thrown when trying to set a parameter 
	 * and the operating mode of the device is AT.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 */
	@Test(expected=InvalidOperatingModeException.class)
	public void testSetParameterATOperatingMode() throws TimeoutException, XBeeException {
		// Configure the operating mode of the device to return AT when asked.
		Mockito.when(xbeeDevice.getOperatingMode()).thenReturn(OperatingMode.AT);
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that a {@code TimeoutException} is thrown when there is a timeout sending the AT 
	 * command to change the parameter.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test(expected=TimeoutException.class)
	public void testSetParameterTimeout() throws TimeoutException, XBeeException, IOException {
		// Configure the sendAtCommand method to throw a timeoutException when called.
		Mockito.doThrow(new TimeoutException()).when(xbeeDevice).sendATCommand(Mockito.any(ATCommand.class));
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code ATCommandException} is thrown when the response of the AT command 
	 * set is null.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test(expected=ATCommandException.class)
	public void testSetParameterNullResponse() throws TimeoutException, XBeeException, IOException {
		// Configure the sendAtCommand method to return a null response when called.
		Mockito.doReturn(null).when(xbeeDevice).sendATCommand(Mockito.any(ATCommand.class));
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code ATCommandException} is thrown when the response status of the AT 
	 * command set is null.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test(expected=ATCommandException.class)
	public void testSetParameterNullResponseStatus() throws TimeoutException, XBeeException, IOException {
		// Configure the mocked ATCommandResponse to return a null response status when asked.
		Mockito.doReturn(null).when(mocketATCommandResponse).getResponseStatus();
		
		// Configure the sendAtCommand method to return the mocked response when called.
		Mockito.doReturn(mocketATCommandResponse).when(xbeeDevice).sendATCommand(Mockito.any(ATCommand.class));
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code ATCommandException} is thrown when the response status of the AT 
	 * command set is not OK.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test(expected=ATCommandException.class)
	public void testSetParameterInvalidResponseStatus() throws TimeoutException, XBeeException, IOException {
		// Configure the mocked ATCommandResponse to return an ERROR response status when asked.
		Mockito.doReturn(ATCommandStatus.ERROR).when(mocketATCommandResponse).getResponseStatus();
		
		// Configure the sendAtCommand method to return the mocked response when called.
		Mockito.doReturn(mocketATCommandResponse).when(xbeeDevice).sendATCommand(Mockito.any(ATCommand.class));
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that an {@code XBeeException} is thrown when there is an IO exception writing in 
	 * the communication interface.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test(expected=XBeeException.class)
	public void testSetParameterIOException() throws TimeoutException, XBeeException, IOException {
		// Configure the sendAtCommand method to throw an IOException when called.
		Mockito.doThrow(new IOException()).when(xbeeDevice).sendATCommand(Mockito.any(ATCommand.class));
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
	
	/**
	 * Test method for {@link com.digi.xbee.api.AbstractXBeeDevice#setParameter(String, byte[])}.
	 * 
	 * <p>Verify that a parameter can be set successfully using the {@code setParameter} method.</p>
	 * 
	 * @throws XBeeException 
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test
	public void testSetParameterSuccess() throws TimeoutException, XBeeException, IOException {
		// Configure the mocked ATCommandResponse to return an OK response status when asked.
		Mockito.doReturn(ATCommandStatus.OK).when(mocketATCommandResponse).getResponseStatus();
		
		// Configure the sendAtCommand method to return the mocked response when called.
		Mockito.doReturn(mocketATCommandResponse).when(xbeeDevice).sendATCommand(Mockito.any(ATCommand.class));
		
		xbeeDevice.setParameter(PARAM_NI, VALUE_NI.getBytes());
	}
}
