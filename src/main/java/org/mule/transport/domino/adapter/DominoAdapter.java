/*
 * $Id: DominoAdapter.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino.adapter;

import lotus.domino.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.transport.domino.DominoConnector;

import org.mule.transport.domino.util.TransformUtil;

import java.util.Map;

/**
 * <code>DominoAdapter</code>
 */
public class DominoAdapter
{

    private static Log logger = LogFactory.getLog(DominoAdapter.class);

    private DominoConnector connector;
    private Session session;
    private Database database;

    public DominoAdapter(DominoConnector connector)
    {
        this.connector = connector;
    }

    public synchronized void doInitialize(String userName, String password,
                                          String serverName, String databaseName) throws Exception
    {
        String host = connector.getHost();
        String port = connector.getPort();

        session  = NotesFactory.createSession(host + ":" + port, userName, password);
        database = session.getDatabase(serverName, databaseName);
    }

    public synchronized void dispose() throws NotesException
    {
		if (database != null)
			database.recycle();
		if (session != null)
			session.recycle();
    }

    /**
     * Invoke create method on Domino database
     * @param payload
     * @return Universal ID
     * @throws NotesException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public String create(Map payload) throws NotesException
    {
    	logger.trace("create");
        Document document = database.createDocument();

        TransformUtil.mapToDocument(database, payload, document);

        if (document.save())
            return document.getUniversalID();
        else
            return null;
    }

    /**
     * Import DXL into Domino database using DXLIMPORTOPTION_CREATE option
     * @param payload
     * @return Universal ID
     * @throws NotesException
     */
	public void createDxl(String payload) throws NotesException
    {
    	logger.trace("create dxl");
        DxlImporter imp = session.createDxlImporter();
        try {
            imp.setDocumentImportOption(DxlImporter.DXLIMPORTOPTION_CREATE);
            imp.importDxl(payload, database);
        } finally {
            imp.recycle();
        }
    }
    
    /**
     * Invoke read method on Domino database
     * @param payload
     * @return Domino document
     * @throws NotesException
     */
    public Document read(String payload) throws NotesException
    {
    	logger.trace("read");
        Document document = database.getDocumentByUNID(payload);
        return document;
    }

    /**
     * Export DXL from Domino database
     * @param payload
     * @return Domino document
     * @throws NotesException
     */
	public String readDxl(String payload) throws NotesException {
		logger.trace("read dxl");
		String dxl = null;
		Document document = database.getDocumentByUNID(payload);
		if (document != null) {
			DxlExporter ixp = null;
			try {
				ixp = session.createDxlExporter();
				ixp.setOutputDOCTYPE(false);
				ixp.setForceNoteFormat(false);
				dxl = ixp.exportDxl(document);
			} finally {
				document.recycle();
				if (ixp != null)
					ixp.recycle();
			}
		}
		return dxl;
	}
    
    /**
     * Invoke update method on Domino database
     * @param payload
     * @return Universal ID
     * @throws NotesException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public String update(Map payload) throws NotesException
    {
    	logger.trace("update");
        String universalID = (String)payload.get("UNID");

        if (universalID != null)
        {
            Document document = database.getDocumentByUNID(universalID);

            TransformUtil.mapToDocument(database, payload, document);

            if (document.save())
                return document.getUniversalID();
            else
                return null;
        }

        return null;
    }

    /**
     * Import DXL into Domino database using DXLIMPORTOPTION_UPDATE_ELSE_IGNORE option
     * @param payload
     * @return Universal ID
     * @throws NotesException
     */
	public void updateDxl(String payload) throws NotesException
    {
    	logger.trace("update dxl");
        DxlImporter imp = session.createDxlImporter();
        try {
            imp.setReplaceDbProperties(false);
            imp.setDocumentImportOption(DxlImporter.DXLIMPORTOPTION_UPDATE_ELSE_IGNORE);
            imp.importDxl(payload, database);
        } finally {
            imp.recycle();
        }
    }
    
    /**
     * Invoke delete method on Domino database
     * @param payload
     * @return True if success, otherwise FALSE
     * @throws NotesException
     */
    public boolean delete(String payload) throws NotesException
    {
    	logger.trace("delete");
        Document document = database.getDocumentByUNID(payload);

        if (document == null)
            return false;

        return document.remove(true);
    }

    /**
    * Find document by query
    * @param payload
    * @return Domino document
    * @throws NotesException
    */
	public Document find(String payload) throws NotesException {
		logger.trace("find");

		Document document = null;
		View v = database.createView("", payload);
		ViewEntryCollection vc = v.getAllEntries();
		ViewEntry entry = vc.getFirstEntry();
		while (entry != null) {

			if (entry.isDocument()) {
				document = entry.getDocument();
				break;
			} else {
				entry.recycle();
			}

			entry = vc.getNextEntry();
		}

		v.recycle();
		vc.recycle();

		return document;
	}
	
    /**
    * Find DXL by query
    * @param payload
    * @return Domino document
    * @throws NotesException
    */
	public String findDxl(String payload) throws NotesException {
		logger.trace("find dxl");

		String dxl = null;
		Document document = find(payload);
		if (document != null) {
			DxlExporter ixp = null;
			try {
				ixp = session.createDxlExporter();
				ixp.setOutputDOCTYPE(false);
				ixp.setForceNoteFormat(false);
				dxl = ixp.exportDxl(document);
			} finally {
				document.recycle();
				if (ixp != null)
					ixp.recycle();
			}
		}

		return dxl;
	}
}
