/*
 * $Id: DominoFunctionalTestCase.java es@mindsinsight.com $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MindsInsight Inc.  All rights reserved.  http://www.mindsinsight.com/
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.domino;

import lotus.domino.Document;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.transport.NullPayload;
import org.mule.transport.domino.jaxb.Item;
import org.mule.transport.domino.jaxb.Noteinfo;
import org.mule.transport.domino.jaxb.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DominoFunctionalTestCase extends AbstractDominoFunctionalTestCase {

	private static String dxlDocPattern = "<?xml version='1.0'?>"
			+ "<document xmlns='http://www.lotus.com/dxl' version='8.5' %s> %s</document>";
	private static String dxlItemPattern = "<item name='%3$s'><%1$s>%2$s</%1$s></item>";
	private static SimpleDateFormat sd = new SimpleDateFormat(
			"yyyyMMdd'T'HHmmss',00'Z");

    public DominoFunctionalTestCase()
    {
        super();
        this.setDisposeManagerPerSuite(true);
    }

     @Override
    protected void doSetUp() throws Exception
    {
        super.doSetUp();

    }

    public void testCreateDelete() throws Exception
    {
        Object[] payloadCreate = new Object[2];
        payloadCreate[0] = createSecurityCredentials();
        payloadCreate[1] = createTestPayload();

        MuleMessage replyOfCreate = client.send("vm://test.create", new DefaultMuleMessage(payloadCreate,
                                                muleContext));

        assertNotNull(replyOfCreate);
        assertNotNull(replyOfCreate.getPayload());
        assertTrue(replyOfCreate.getPayload() instanceof String);

        Object[] payloadRead = new Object[2];
        payloadRead[0] = createSecurityCredentials();
        payloadRead[1] = replyOfCreate.getPayload();

        MuleMessage replyOfRead = client.send("vm://test.read", new DefaultMuleMessage(payloadRead,
                                              muleContext));

        assertNotNull(replyOfRead);
        assertNotNull(replyOfRead.getPayload());
        assertTrue(replyOfRead.getPayload() instanceof Document);

        Object[] payloadFind = new Object[2];
        payloadFind[0] = createSecurityCredentials();
        payloadFind[1] = "Subject='My Test Meeting'";

        MuleMessage replyOfFind = client.send("vm://test.find", new DefaultMuleMessage(payloadFind,
                                                muleContext));

        assertNotNull(replyOfFind);
        assertNotNull(replyOfFind.getPayload());
        assertTrue(replyOfFind.getPayload() instanceof Document);

        MuleMessage replyOfFindDocument = client.send("vm://test.find_document", new DefaultMuleMessage(payloadFind,
                muleContext));

		assertNotNull(replyOfFindDocument);
		assertNotNull(replyOfFindDocument.getPayload());
		assertTrue(replyOfFindDocument.getPayload() instanceof org.mule.transport.domino.jaxb.Document);

        Object[] payloadUpdate = new Object[2];
        payloadUpdate[0] = createSecurityCredentials();
        payloadUpdate[1] = updateTestPayload((String) replyOfCreate.getPayload());

        MuleMessage replyOfUpdate = client.send("vm://test.update", new DefaultMuleMessage(payloadUpdate,
                                                muleContext));

        assertNotNull(replyOfUpdate);
        assertNotNull(replyOfUpdate.getPayload());
        assertTrue(replyOfUpdate.getPayload() instanceof String);

        Object[] payloadDelete = new Object[2];
        payloadDelete[0] = createSecurityCredentials();
        payloadDelete[1] = replyOfUpdate.getPayload();

        MuleMessage replyOfDelete = client.send("vm://test.delete", new DefaultMuleMessage(payloadDelete,
                                                muleContext));

        assertNotNull(replyOfDelete);
        assertNotNull(replyOfDelete.getPayload());
        assertTrue((Boolean)replyOfDelete.getPayload());
    }

    public void testCreateDeleteDxl() throws Exception
    {
        Object[] payloadCreate = new Object[2];
        payloadCreate[0] = createSecurityCredentials();
        payloadCreate[1] = generateDxl(createTestPayload());
        
        MuleMessage replyOfCreate = client.send("vm://test.create_dxl", new DefaultMuleMessage(payloadCreate,
                                                muleContext));

        assertNotNull(replyOfCreate);
        assertNotNull(replyOfCreate.getPayload());
        assertTrue(replyOfCreate.getPayload() instanceof NullPayload);

        Object[] payloadFind = new Object[2];
        payloadFind[0] = createSecurityCredentials();
        payloadFind[1] = "Subject='My Test Meeting'";

        MuleMessage replyOfFind = client.send("vm://test.find", new DefaultMuleMessage(payloadFind,
                                                muleContext));
        
        assertNotNull(replyOfFind);
        assertNotNull(replyOfFind.getPayload());
        assertTrue(replyOfFind.getPayload() instanceof Document);
        
        Document document = (Document)replyOfFind.getPayload();
        String id = document.getUniversalID();
        assertNotNull(id);
        
        Object[] payloadFindDxl = new Object[2];
        payloadFindDxl[0] = createSecurityCredentials();
        payloadFindDxl[1] = "Subject='My Test Meeting'";

        MuleMessage replyOfFindDxl = client.send("vm://test.find_dxl", new DefaultMuleMessage(payloadFindDxl,
                muleContext));

		assertNotNull(replyOfFindDxl);
		assertNotNull(replyOfFindDxl.getPayload());
		assertTrue(replyOfFindDxl.getPayload() instanceof String);

        Object[] payloadRead = new Object[2];
        payloadRead[0] = createSecurityCredentials();
        payloadRead[1] = id;

        MuleMessage replyOfRead = client.send("vm://test.read_dxl", new DefaultMuleMessage(payloadRead,
                                              muleContext));

        assertNotNull(replyOfRead);
        assertNotNull(replyOfRead.getPayload());
        assertTrue(replyOfRead.getPayload() instanceof String);

        Object[] payloadUpdate = new Object[2];
        payloadUpdate[0] = createSecurityCredentials();
        payloadUpdate[1] = generateDxl(updateTestPayload(id));

        MuleMessage replyOfUpdate = client.send("vm://test.update_dxl", new DefaultMuleMessage(payloadUpdate,
                                                muleContext));

        assertNotNull(replyOfUpdate);
        assertNotNull(replyOfUpdate.getPayload());
        assertTrue(replyOfUpdate.getPayload() instanceof NullPayload);

        Object[] payloadUpdateDocument = new Object[2];
        payloadUpdateDocument[0] = createSecurityCredentials();

        Object[] payloadDelete = new Object[2];
        payloadDelete[0] = createSecurityCredentials();
        payloadDelete[1] = id;

        MuleMessage replyOfDelete = client.send("vm://test.delete", new DefaultMuleMessage(payloadDelete,
                                                muleContext));

        assertNotNull(replyOfDelete);
        assertNotNull(replyOfDelete.getPayload());
        assertTrue((Boolean)replyOfDelete.getPayload());
    }

    public void testDocumentToDxl() throws Exception
    {
        org.mule.transport.domino.jaxb.Document jaxbDocument = new org.mule.transport.domino.jaxb.Document();
		Item item = new Item();
		item.setName("Subject");
		Text t = new Text();
		t.getContent().add("Test Subject");
		item.setText(t);
		jaxbDocument.getItem().add(item);
		
        MuleMessage replyOfUpdateDocument = client.send("vm://test.document_to_dxl", new DefaultMuleMessage(jaxbDocument,
                                                muleContext));

        assertNotNull(replyOfUpdateDocument);
        assertNotNull(replyOfUpdateDocument.getPayload());
        assertTrue(replyOfUpdateDocument.getPayload() instanceof String);        
    }
    
    public void testDxlToDocument() throws Exception
    {
    	String dxl = generateDxl(createTestPayload());
		
        MuleMessage replyOfUpdateDocument = client.send("vm://test.dxl_to_document", new DefaultMuleMessage(dxl,
                                                muleContext));

        assertNotNull(replyOfUpdateDocument);
        assertNotNull(replyOfUpdateDocument.getPayload());
        assertTrue(replyOfUpdateDocument.getPayload() instanceof org.mule.transport.domino.jaxb.Document);
    }

    private Map<String, String> createSecurityCredentials()
    {
        Map<String, String> securityCredentials = new HashMap<String, String>();

        securityCredentials.put("UserName", properties.getProperty("user"));
        securityCredentials.put("Password", properties.getProperty("password"));
        securityCredentials.put("ServerName", properties.getProperty("servername"));
        securityCredentials.put("DatabaseName", properties.getProperty("databasename"));

        return securityCredentials;
    }

    private Map<String, Object> updateTestPayload(String universalID)
    {
        Map<String, Object> document = new HashMap<String, Object>();

        document.put("UNID", universalID);
        document.put("Subject", "UPDATED at " + new Date());

        return document;
    }
    
    private Map<String, Object> createTestPayload()
    {
        Map<String, Object> document = new HashMap<String, Object>();

        document.put("Form","Appointment");
        document.put("Chair", "User1");
        document.put("AltChair", "User1");
        document.put("$PublicAccess","1");

        Date now = new Date();

        document.put("STARTDATETIME",now);
        document.put("EndDateTime",now);
        document.put("CalendarDateTime", now);
        document.put("RepeatDates", now);
        document.put("RepeatInstanceDates", now);
        document.put("RepeatEndDates",now);
        document.put("Subject","My Test Meeting");
        document.put("StartTimeZone",new String("Z=-3005$DO=0$ZX=35$ZN=India"));
        document.put("EndTimeZone" ,new String("Z=-3005$DO=0$ZX=35$ZN=India"));
        document.put("AppointmentType","0");
        document.put("MeetingType", "1");
        document.put("RepeatUnit","D");
        document.put("RepeatHow","U");
        document.put("RepeatUntil",now);
        document.put("RepeatForUnit","D");
        document.put("RepeatWeekends","D");
        document.put("Repeats","1");

        document.put("RepeatStartDate",now);
        document.put("RepeatEndDate", now);
        document.put("lastDate",now);
        document.put("RepeatInterval","1");
        document.put("$NoPurge",now);
        document.put("RepeatAdjust","");
        document.put("RepeatFromEnd","");
        document.put("RepeatFor","2");
        document.put("_ViewIcon",160);
        document.put("OrgTable","CO");
        document.put("$HFFlags","1");
        document.put("SchedulerSwitcher","1");
        document.put("SequenceNum",1);
        document.put("UpdateSeq",1);

        return document;
    }

    protected String getConfigResources()
    {
        return "domino-functional-test.xml";
    }

	private String generateDxl(Map<String, Object> docItems) {

		StringBuilder sb = new StringBuilder();
		String formValue = null;
		for (Map.Entry<String, Object> item : docItems.entrySet()) {
			String name = item.getKey();
			Object obj = item.getValue();
			if ("form".equalsIgnoreCase(name)) {
				formValue = "" + obj;
			} else {
				String type = "text";
				String value = "";
				if (obj instanceof Date) {
					type = "datetime";
					value = sd.format((Date) obj).substring(0, 21);
				} else if (obj instanceof Number) {
					type = "number";
					value = "" + obj;
				} else {
					value = "" + obj;
				}
				sb.append(String.format(dxlItemPattern, type, value, name)
						+ "\n");
			}

		}

		return String.format(dxlDocPattern, formValue != null ? "form='"
				+ formValue + "'" : "", sb.toString());
	}
}
