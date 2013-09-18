/*
 * $Id: DominoNamespaceHandler.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino.config;

import org.mule.config.spring.handlers.AbstractMuleNamespaceHandler;
import org.mule.config.spring.parsers.specific.MessageProcessorDefinitionParser;
import org.mule.endpoint.URIBuilder;
import org.mule.transport.domino.DominoConnector;
import org.mule.transport.domino.transformers.DocumentToDxl;
import org.mule.transport.domino.transformers.DxlToDocument;

/**
 * <code>DominoNamespaceHandler</code>
 */
public class DominoNamespaceHandler extends AbstractMuleNamespaceHandler
{
    public void init()
    {
        registerStandardTransportEndpoints(DominoConnector.DOMINO, URIBuilder.PATH_ATTRIBUTES);
        registerConnectorDefinitionParser(DominoConnector.class);
        
        registerBeanDefinitionParser("document-to-dxl", new MessageProcessorDefinitionParser(DocumentToDxl.class));
        registerBeanDefinitionParser("dxl-to-document", new MessageProcessorDefinitionParser(DxlToDocument.class));
    }
}
