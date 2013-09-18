/*
 * $Id: DominoConnectorTestCase.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.mule.api.transport.Connector;
import org.mule.transport.AbstractConnectorTestCase;

public class DominoConnectorTestCase extends AbstractConnectorTestCase
{

    @Override
    public Connector createConnector() throws Exception
    {

        DominoConnector connector = new DominoConnector(muleContext);
        connector.setName("DominoConnector");
        return connector;
    }

    @Override
    public String getTestEndpointURI()
    {
        return "domino://localhost:6666/create";
    }

    @Override
    public Object getValidMessage() throws Exception
    {
        return "Hello".getBytes();
    }


    public void testProperties() throws Exception
    {
        DominoConnector connector = (DominoConnector)getConnector();

        String host = "localhost";
        connector.setHost(host);

        assertEquals(host, connector.getHost());

        String port = "6666";
        connector.setPort(port);

        assertEquals(port, connector.getPort());

        String path = "/create";
        connector.setPath(path);

        assertEquals(path, connector.getPath());
    }

    public void testConnectorListenerSupport() throws Exception
    {
        // No listener support
        assertTrue(true);
    }
}
