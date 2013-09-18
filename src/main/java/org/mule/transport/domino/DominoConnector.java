/*
 * $Id: DominoConnector.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transport.MessageReceiver;
import org.mule.transport.AbstractConnector;
import org.mule.transport.domino.adapter.DominoAdapter;

/**
 * A Connector for Domino
 */
public class DominoConnector extends AbstractConnector
{

    private static Log logger = LogFactory.getLog(AbstractConnector.class);
    public static final String DOMINO = "domino";

    private String host;
    private String port;
    private String path;

    private DominoAdapter adapter;

    public DominoConnector(MuleContext context)
    {
        super(context);
    }
       
	/** {@inheritDoc}
	 */
    @Override
    public void doInitialise() throws InitialisationException
    {
        logger.info("************DominoConnector.doInitialise()*****************");            
    }

	/** {@inheritDoc}
	 */
    @Override
    public void doConnect() throws Exception
    {
        logger.info("************DominoConnector.doConnect()*****************");
    }

	/** {@inheritDoc}
	 */
    @Override
    public void doDisconnect() throws Exception
    {
        logger.info("************DominoConnector.doDisconnect()*****************");

    }

	/** {@inheritDoc}
	 */
    @Override
    public void doStart() throws MuleException
    {
        logger.info("************DominoConnector.doStart()*****************");

        this.adapter = new DominoAdapter(this);
    }

	/** {@inheritDoc}
	 */
    @Override
    public void doStop() throws MuleException
    {
        logger.info("************DominoConnector.doStop()*****************");
    }

	/** {@inheritDoc}
	 */
    @Override
    public void doDispose()
    {
        logger.info("************DominoConnector.doDispose()*****************");                
    }

	/** {@inheritDoc}
	 */
    @Override
    protected MessageReceiver createReceiver(FlowConstruct flowConstruct, InboundEndpoint endpoint) throws Exception {
        return super.createReceiver(flowConstruct, endpoint);
    }

    public String getProtocol()
    {
        return DOMINO;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get a Domino adapter
     * @return Domino adapter
     */
    public DominoAdapter getAdapter() {
        return adapter;
    }

    /**
     * Specify the Domino adapter
     * @param adapter
     */
    public void setAdapter(DominoAdapter adapter) {
        this.adapter = adapter;
    }
}
