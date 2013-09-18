/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino.transformers;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.transport.domino.jaxb.Document;

/**
 * Transformer to convert DXL string to Document object
 */
public class DxlToDocument extends AbstractMessageTransformer {

	public DxlToDocument() {
		registerSourceType(DataTypeFactory.TEXT_STRING);
		setReturnDataType(DataTypeFactory.create(Document.class));
		setName(DxlToDocument.class.getSimpleName());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {

		Document document = null;
		try {
			String dxl = (String) message.getPayload();

			JAXBContext jc = JAXBContext
					.newInstance("org.mule.transport.domino.jaxb");
			Unmarshaller u = jc.createUnmarshaller();
			JAXBElement<Document> element = (JAXBElement<Document>)u.unmarshal(new StringReader(dxl));
			document = element.getValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new TransformerException(this, e);
		}
		return document;
	}

}
