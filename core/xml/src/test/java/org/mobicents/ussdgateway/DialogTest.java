package org.mobicents.ussdgateway;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DialogTest {

	private EventsSerializeFactory factory = null;

	@BeforeClass
	public void setUpClass() throws Exception {
		factory = new EventsSerializeFactory();
	}

	@AfterClass
	public void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() {
	}

	@AfterMethod
	public void tearDown() {
	}

	@Test
	public void testProcessUnstructuredSSRequestSerialization() throws XMLStreamException, MAPException {

		AddressString destReference = new AddressStringImpl(AddressNature.international_number,
				NumberingPlan.land_mobile, "204208300008002");
		AddressString origReference = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
				"31628968300");

		ISDNAddressStringImpl isdnAddress = new ISDNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "79273605819");
		AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category3);
		CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
		USSDString ussdStr = new USSDStringImpl("*234#", cbsDataCodingScheme, null);
		ProcessUnstructuredSSRequestImpl processUnstructuredSSRequestIndication = new ProcessUnstructuredSSRequestImpl(
				cbsDataCodingScheme, ussdStr, alertingPattern, isdnAddress);

		Dialog original = new Dialog(DialogType.BEGIN, 1234l, destReference, origReference,
				processUnstructuredSSRequestIndication);

		byte[] serializedEvent = this.factory.serialize(original);
		System.out.println(new String(serializedEvent));

		Dialog copy = this.factory.deserialize(serializedEvent);

		assertNotNull(copy);

		assertEquals(copy.getType(), original.getType());
		assertEquals(copy.getId(), original.getId());
		ProcessUnstructuredSSRequestImpl copyUSSR = (ProcessUnstructuredSSRequestImpl) copy.getMAPMessage();
		ProcessUnstructuredSSRequestImpl originalUSSR = (ProcessUnstructuredSSRequestImpl) original
				.getMAPMessage();
		assertEquals(copyUSSR.getUSSDString(), originalUSSR.getUSSDString());

	}

	@Test
	public void testProcessUnstructuredSSResponseSerialization() throws XMLStreamException, MAPException {
		CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
		USSDString ussdStr = new USSDStringImpl("Thank You!",cbsDataCodingScheme, null);
		ProcessUnstructuredSSResponseImpl processUnstructuredSSResponseIndication = new ProcessUnstructuredSSResponseImpl(
				cbsDataCodingScheme, ussdStr);

		Dialog original = new Dialog(DialogType.END, 1234l, processUnstructuredSSResponseIndication);

		byte[] serializedEvent = this.factory.serialize(original);
		System.out.println(new String(serializedEvent));

		Dialog copy = this.factory.deserialize(serializedEvent);

		assertNotNull(copy);

		assertEquals(copy.getType(), original.getType());
		assertEquals(copy.getId(), original.getId());
		ProcessUnstructuredSSResponse copyUSSR = (ProcessUnstructuredSSResponse) copy
				.getMAPMessage();
		ProcessUnstructuredSSResponse originalUSSR = (ProcessUnstructuredSSResponse) original
				.getMAPMessage();
		assertEquals(copyUSSR.getUSSDString(), originalUSSR.getUSSDString());

	}

	@Test
	public void testUnstructuredSSRequestSerialization() throws XMLStreamException, MAPException {
		CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
		USSDString ussdStr = new USSDStringImpl("USSD String : Hello World\n 1. Balance\n 2. Texts Remaining", cbsDataCodingScheme, null);
		UnstructuredSSRequestImpl unstructuredSSRequestIndication = new UnstructuredSSRequestImpl(
				cbsDataCodingScheme, ussdStr, null, null);

		Dialog original = new Dialog(DialogType.CONTINUE, 1234l, null, null, unstructuredSSRequestIndication);

		byte[] serializedEvent = this.factory.serialize(original);
		System.out.println(new String(serializedEvent));

		Dialog copy = this.factory.deserialize(serializedEvent);

		assertNotNull(copy);

		assertEquals(copy.getType(), original.getType());
		assertEquals(copy.getId(), original.getId());

	}

	@Test
	public void testUnstructuredSSResponseSerialization() throws XMLStreamException, MAPException {
		CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
		USSDString ussdStr = new USSDStringImpl("1", cbsDataCodingScheme, null);
		UnstructuredSSResponseImpl unstructuredSSResponseIndication = new UnstructuredSSResponseImpl(
				cbsDataCodingScheme, ussdStr);

		Dialog original = new Dialog(DialogType.CONTINUE, 1234l, unstructuredSSResponseIndication);

		byte[] serializedEvent = this.factory.serialize(original);
		System.out.println(new String(serializedEvent));

		Dialog copy = this.factory.deserialize(serializedEvent);

		assertNotNull(copy);

		assertEquals(copy.getType(), original.getType());
		assertEquals(copy.getId(), original.getId());
		UnstructuredSSResponse copyUSSR = (UnstructuredSSResponse) copy.getMAPMessage();
		UnstructuredSSResponse originalUSSR = (UnstructuredSSResponse) original.getMAPMessage();
		assertEquals(copyUSSR.getUSSDString(), originalUSSR.getUSSDString());

	}

	@Test
	public void testUnstructuredSSNotifyRequestIndicationSerialization() throws XMLStreamException, MAPException {

		AddressString destReference = new AddressStringImpl(AddressNature.international_number,
				NumberingPlan.land_mobile, "204208300008002");
		AddressString origReference = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
				"31628968300");
		CBSDataCodingScheme cbsDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
		USSDString ussdStr = new USSDStringImpl(
				"Your new balance is 34.38 AFN and expires on 30.07.2012. Cost of last event was 0.50 AFN.", cbsDataCodingScheme, null);
		UnstructuredSSNotifyRequestImpl unstructuredSSNotifyRequestIndication = new UnstructuredSSNotifyRequestImpl(
				cbsDataCodingScheme, ussdStr, null, null);

		Dialog original = new Dialog(DialogType.BEGIN, 1234l, destReference, origReference,
				unstructuredSSNotifyRequestIndication);

		byte[] serializedEvent = this.factory.serialize(original);
		System.out.println(new String(serializedEvent));

		Dialog copy = this.factory.deserialize(serializedEvent);

		assertNotNull(copy);

		assertEquals(copy.getType(), original.getType());
		assertEquals(copy.getId(), original.getId());
		UnstructuredSSNotifyRequest copyUSSR = (UnstructuredSSNotifyRequest) copy.getMAPMessage();
		UnstructuredSSNotifyRequest originalUSSR = (UnstructuredSSNotifyRequest) original
				.getMAPMessage();
		assertEquals(copyUSSR.getUSSDString(), originalUSSR.getUSSDString());

	}

	@Test
	public void testDialogAbortSerialization() throws XMLStreamException {
		Dialog original = new Dialog(DialogType.ABORT, 1234l, null, null);

		byte[] serializedEvent = this.factory.serialize(original);
		System.out.println(new String(serializedEvent));

		Dialog copy = this.factory.deserialize(serializedEvent);

		assertNotNull(copy);
		
		assertEquals(copy.getType(), original.getType());
	}

}
