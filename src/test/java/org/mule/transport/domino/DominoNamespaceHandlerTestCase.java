/*
 * $Id: DominoNamespaceHandlerTestCase.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

public class DominoNamespaceHandlerTestCase extends AbstractDominoFunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "domino-namespace-config.xml";
    }

    public void testDominoConfig() throws Exception
    {
        DominoConnector connector = (DominoConnector) muleContext.getRegistry().lookupConnector("dominoConnector");
        assertNotNull(connector);
        assertTrue(connector.isConnected());
        assertTrue(connector.isStarted());
        assertEquals(properties.getProperty("host"), connector.getHost());
        assertEquals(properties.getProperty("port"), connector.getPort());
        assertEquals("/create", connector.getPath());
    }
}
