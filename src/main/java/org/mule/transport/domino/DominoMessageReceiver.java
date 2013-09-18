/*
 * $Id: DominoMessageReceiver.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.AbstractMessageReceiver;
import org.mule.transport.ConnectException;

/**
 * <code>DominoMessageReceiver</code>
 */
public class DominoMessageReceiver extends AbstractMessageReceiver {

     public DominoMessageReceiver(Connector connector, FlowConstruct flowConstruct,
                              InboundEndpoint endpoint)
            throws CreateException
    {
        super(connector, flowConstruct, endpoint);
    }

 	/** {@inheritDoc}
 	 */
    @Override
    public void doConnect() throws ConnectException
    {

    }

	/** {@inheritDoc}
	 */
    @Override
    public void doDisconnect() throws ConnectException
    {

    }

	/** {@inheritDoc}
	 */
    @Override
    public void doDispose()
    {

    }

}
