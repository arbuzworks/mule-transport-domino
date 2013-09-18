/*
 * $Id: DominoEndpointTestCase.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.mule.api.endpoint.EndpointURI;
import org.mule.endpoint.MuleEndpointURI;
import org.mule.tck.AbstractMuleTestCase;

public class DominoEndpointTestCase extends AbstractMuleTestCase
{

    public void testValidEndpointURI() throws Exception
    {

        EndpointURI url = new MuleEndpointURI("domino://localhost:6666/create", muleContext);

        assertEquals("domino", url.getScheme());
        assertEquals(6666, url.getPort());
        assertEquals("localhost", url.getHost());
        assertEquals("/create", url.getPath());
        assertEquals("domino://localhost:6666/create", url.getUri().toString());
        assertEquals(0, url.getParams().size());

    }
}
