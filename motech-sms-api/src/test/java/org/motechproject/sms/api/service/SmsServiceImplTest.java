package org.motechproject.sms.api.service;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.EventRelay;
import org.motechproject.model.MotechEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.sms.api.service.SmsServiceImpl.*;

public class SmsServiceImplTest {

    private SmsService smsService;

    @Mock
    private EventRelay eventRelay;

    @Before
    public void setup() {
        initMocks(this);
        smsService = new SmsServiceImpl(eventRelay);
    }

    @Test
    public void shouldRaiseASendSmsEventWithMessageAndRecipient() {
        smsService.sendSMS("9876543210", "This is a test message");

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());

        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();
        assertEquals("This is a test message", (String) eventMessageSent.getParameters().get(MESSAGE));
        assertEquals(Arrays.asList("9876543210"), eventMessageSent.getParameters().get(RECIPIENTS));
    }

    @Test
    public void shouldRaiseASendSmsEventWithMessageMulitpleRecipients() {
        ArrayList<String> recipients = new ArrayList<String>() {{
            add("123");
            add("456");
            add("789");
        }};
        smsService.sendSMS(recipients, "This is a test message");

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());

        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();
        assertEquals("This is a test message", (String) eventMessageSent.getParameters().get(MESSAGE));
        assertEquals(recipients, eventMessageSent.getParameters().get(RECIPIENTS));
    }

    @Test
    public void shouldRaiseASendSmsEventWithMessageAndRecipientAndScheduledDeliveryTime() {
        smsService.sendSMS("123", "This is a test message", new DateTime(2011, 12, 23, 13, 50, 0, 0));

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());

        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();
        assertEquals("This is a test message", (String) eventMessageSent.getParameters().get(MESSAGE));
        assertEquals(Arrays.asList("123"), eventMessageSent.getParameters().get(RECIPIENTS));
        assertEquals(new DateTime(2011, 12, 23, 13, 50, 0, 0), eventMessageSent.getParameters().get(DELIVERY_TIME));
    }

    @Test
    public void shouldRaiseASendSmsEventWithMessageAndMultipleRecipientsAndScheduledDeliveryTime() {
        ArrayList<String> recipients = new ArrayList<String>() {{
            add("123");
            add("456");
            add("789");
        }};
        smsService.sendSMS(recipients, "This is a test message", new DateTime(2011, 12, 23, 13, 50, 0, 0));

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());

        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();
        assertEquals("This is a test message", (String) eventMessageSent.getParameters().get(MESSAGE));
        assertEquals(recipients, eventMessageSent.getParameters().get(RECIPIENTS));
        assertEquals(new DateTime(2011, 12, 23, 13, 50, 0, 0), eventMessageSent.getParameters().get(DELIVERY_TIME));
    }
}