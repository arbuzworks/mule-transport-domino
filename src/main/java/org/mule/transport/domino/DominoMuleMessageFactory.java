/*
 * $Id: DominoMuleMessageFactory.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.mule.api.MuleContext;
import org.mule.api.transport.MessageTypeNotSupportedException;
import org.mule.transport.AbstractMuleMessageFactory;

/**
 * <code>DominoMuleMessageFactory</code>
 */
public class DominoMuleMessageFactory extends AbstractMuleMessageFactory
{

    public DominoMuleMessageFactory(MuleContext context)
    {
        super(context);
    }

	/** {@inheritDoc}
	 */
    @Override
    protected Object extractPayload(Object transportMessage, String encoding) throws Exception
    {
        if (transportMessage == null)
        {
            throw new MessageTypeNotSupportedException(null, getClass());
        }
        else
        {
            return transportMessage;
        }
    }

	/** {@inheritDoc}
	 */
    @Override
    protected Class<?>[] getSupportedTransportMessageTypes()
    {
        return new Class[]{Object.class};
    }
}
