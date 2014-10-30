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
package com.digi.xbee.api.listeners;

import com.digi.xbee.api.models.XBeeMessage;

/**
 * This interface defines the required methods that should be implemented to 
 * behave as a data listener and be notified when new data is received from an 
 * XBee device of the network.
 */
public interface IDataReceiveListener {

	/**
	 * Called when data is received from a remote node.
	 * 
	 * @param xbeeMessage XBeeMessage object containing the data, the address 
	 *                    of the remote XBee device that sent the data and a flag 
	 *                    indicating if the data was sent via broadcast.
	 */
	public void dataReceived(XBeeMessage xbeeMessage);
}
