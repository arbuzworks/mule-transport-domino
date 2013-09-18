/*
 * $Id: AbstractDominoFunctionalTestCase.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractDominoFunctionalTestCase extends FunctionalTestCase
{

    protected String username;
    protected String password;

    protected String host;
    protected String port;
    protected String serverName;
    protected String databaseName;

    protected MuleClient client;

    protected Properties properties;

    @Override
    protected void doSetUp() throws Exception
    {
        super.doSetUp();
        client = new MuleClient(muleContext);
        properties = new Properties();

        InputStream propertiesFile = this.getClass().getClassLoader().getResourceAsStream("test.properties");
        properties.load(propertiesFile);

        username = properties.getProperty("user");
        password = properties.getProperty("password");
        host = properties.getProperty("host");
        port = properties.getProperty("port");

        serverName = properties.getProperty("servername");;
        databaseName = properties.getProperty("databasename");

    }

}