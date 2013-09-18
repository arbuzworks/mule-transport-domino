/*
 * $Id: DominoMessageDispatcher.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import java.util.Map;

import lotus.domino.Document;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.message.DefaultExceptionPayload;
import org.mule.transport.AbstractMessageDispatcher;
import org.mule.transport.domino.adapter.DominoAdapter;

/**
 * Will dispatch Mule events to clients that are listening to this endpoint.
 * <code>DominoMessageDispatcher</code>
 */
public class DominoMessageDispatcher extends AbstractMessageDispatcher
{

    private static Log logger
        = LogFactory.getLog(AbstractMessageDispatcher.class);

    DominoConnector connector = null;

    public DominoMessageDispatcher(OutboundEndpoint endpoint)
    {
        super(endpoint);
        connector = (DominoConnector)endpoint.getConnector();        
    }

	/** {@inheritDoc}
	 */
    @Override
    public void doConnect() throws Exception
    {
        logger.info("*****DominoMessageDispatcher.doConnect()*****");

    }

	/** {@inheritDoc}
	 */
    @Override
    public void doDisconnect() throws Exception
    {
        logger.info("*****DominoMessageDispatcher.doDisconnect()*****");
    }

	/** {@inheritDoc}
	 */
    @Override
    public void doDispatch(MuleEvent event) throws Exception
    {
        logger.info("*****DominoMessageDispatcher.doDispatch()*****");        

        doSend(event);
    }

    @SuppressWarnings("rawtypes")
	@Override
    public MuleMessage doSend(MuleEvent event) throws Exception
    {
        logger.info("*****DominoMessageDispatcher.doSend()*****");        

        Object[] payload = (Object[])event.getMessage().getPayload();
        Map securityCredentials = (Map)payload[0];

        try
        {
            this.connector.getAdapter().doInitialize((String)securityCredentials.get("UserName"),
                                                     (String)securityCredentials.get("Password"),
                                                     (String)securityCredentials.get("ServerName"),
                                                     (String)securityCredentials.get("DatabaseName"));

            String path = ((DominoConnector)event.getEndpoint().getConnector()).getPath();

            if (path == null)
            {
                path = event.getEndpoint().getEndpointURI().getPath();
            }

            if ("/create".equalsIgnoreCase(path))
            {
                doCreateObject(event);
            }
            else if ("/read".equalsIgnoreCase(path))
            {
                doReadObject(event);
            }
            else if ("/update".equalsIgnoreCase(path))
            {
                doUpdateObject(event);
            }
            else if ("/delete".equalsIgnoreCase(path))
            {
                doDeleteObject(event);
            }
            else if ("/find".equalsIgnoreCase(path))
            {
                doFindObject(event);
            }            
            else if ("/create_dxl".equalsIgnoreCase(path))
            {
                doCreateDxl(event);
            }
            else if ("/read_dxl".equalsIgnoreCase(path))
            {
                doReadDxl(event);
            }
            else if ("/update_dxl".equalsIgnoreCase(path))
            {
                doUpdateDxl(event);
            }
            else if ("/find_dxl".equalsIgnoreCase(path))
            {
                doFindDxl(event);
            }            
            else
            {
                throw new IllegalArgumentException("Path '" +  path + "' is not acceptable");
            }

            return new DefaultMuleMessage(event.getMessage().getPayload(), event.getMuleContext());
        }
        finally
        {
            this.connector.getAdapter().dispose();
        }
    }

    @SuppressWarnings("rawtypes")
	protected void doCreateObject(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        Map document = (Map)payload[1];

        try
        {
            String universalID = adapter.create(document);
            if (universalID != null)
            {
                event.getMessage().setPayload(universalID);
                if (logger.isDebugEnabled())
                {
                    logger.debug("A Document was created with an id of: " + universalID);
                }
            }
            else
            {
                Exception e = new Exception("Creation of document with an id of " + universalID + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }

	protected void doCreateDxl(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String dxl = (String)payload[1];

        try
        {
            adapter.createDxl(dxl);
            event.getMessage().setPayload(null);
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }
    
    protected void doReadObject(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String universalID = (String)payload[1];

        try
        {
            Document document = adapter.read(universalID);
            if (document != null)
            {
                event.getMessage().setPayload(document);
                if (logger.isDebugEnabled())
                {
                    logger.debug("Read the document with an id of: " + document.getUniversalID());
                }
            }
            else
            {
                Exception e = new Exception("Reading the document with an id of: " + payload + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }

    protected void doReadDxl(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String universalID = (String)payload[1];

        try
        {
            String dxl = adapter.readDxl(universalID);
            if (dxl != null)
            {
                event.getMessage().setPayload(dxl);
            }
            else
            {
                Exception e = new Exception("Reading the DXL with an id of: " + payload + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }
    
    @SuppressWarnings("rawtypes")
	protected void doUpdateObject(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        Map document = (Map)payload[1];

        try
        {
            String universalID = adapter.update(document);
            if (universalID != null)
            {
                event.getMessage().setPayload(universalID);
                if (logger.isDebugEnabled())
                {
                    logger.debug("A Document with an id of: " + universalID + " was updated");
                }
            }
            else
            {
                Exception e = new Exception("Updating of document with an id of " + universalID + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }

	protected void doUpdateDxl(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String dxl = (String)payload[1];

        try
        {
            adapter.updateDxl(dxl);
            event.getMessage().setPayload(null);
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }
    
    protected void doDeleteObject(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String universalID = (String)payload[1];

        try
        {
            boolean result = adapter.delete(universalID);
            if (result)
            {
                event.getMessage().setPayload(result);
                if (logger.isDebugEnabled())
                {
                    logger.debug("A Document was deleted with an id of: " + payload);
                }
            }
            else
            {
                Exception e = new Exception("Deletion of document with an id of " + payload + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }

    protected void doFindObject(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String query = (String)payload[1];

        try
        {
            Document document = adapter.find(query);
            if (document != null)
            {
                event.getMessage().setPayload(document);
                if (logger.isDebugEnabled())
                {
                    logger.debug("Find the document with an id of: " + document.getUniversalID());
                }
            }
            else
            {
                Exception e = new Exception("Finding the document with an id of: " + payload + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }

    protected void doFindDxl(MuleEvent event) throws Exception
    {
        DominoAdapter adapter = ((DominoConnector)event.getEndpoint().getConnector()).getAdapter();
        Object[] payload = (Object[])event.getMessage().getPayload();
        String query = (String)payload[1];

        try
        {
            String dxl = adapter.findDxl(query);
            if (dxl != null)
            {
                event.getMessage().setPayload(dxl);
            }
            else
            {
                Exception e = new Exception("Finding the DXL with an id of: " + payload + " failed");
                event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
            }
        }
        catch(Exception e)
        {
        	logger.error(e.getMessage(), e);
            event.getMessage().setExceptionPayload(new DefaultExceptionPayload(e));
        }
    }
    
	/** {@inheritDoc}
	 */
    @Override
    public void doDispose()
    {
        logger.info("*****DominoMessageDispatcher.doDispose()*****");            
    }

}

