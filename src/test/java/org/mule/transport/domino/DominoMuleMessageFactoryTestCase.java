/*
 * $Id: DominoMuleMessageFactoryTestCase.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import org.mule.api.transport.MuleMessageFactory;
import org.mule.transport.AbstractMuleMessageFactoryTestCase;

import java.util.HashMap;

public class DominoMuleMessageFactoryTestCase extends AbstractMuleMessageFactoryTestCase
{

    public DominoMuleMessageFactoryTestCase()
    {
        super();
        runUnsuppoprtedTransportMessageTest = false;
    }

    @Override
    protected Object getValidTransportMessage() throws Exception
    {
        Object[] payloadDelete = new Object[2];
        payloadDelete[0] = new HashMap<Object, Object>();
        payloadDelete[1] = "123456789";

        return payloadDelete;
    }

    @Override
    protected MuleMessageFactory doCreateMuleMessageFactory()
    {
        return new DominoMuleMessageFactory(muleContext);
    }

}

