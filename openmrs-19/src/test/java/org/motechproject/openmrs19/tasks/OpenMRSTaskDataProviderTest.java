package org.motechproject.openmrs19.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.BY_UUID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.MOTECH_ID;
import static org.motechproject.openmrs19.tasks.OpenMRSTasksConstants.UUID;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderTest {

    @Mock
    private OpenMRSEncounterService encounterService;

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSProviderService providerService;

    @Mock
    private ResourceLoader resourceLoader;

    private OpenMRSTaskDataProvider taskDataProvider;

    @Before
    public void setUp() {
        taskDataProvider = new OpenMRSTaskDataProvider(resourceLoader, encounterService, patientService, providerService);
    }

    @Test
    public void shouldReturnNullWhenClassIsNotSupported() {
        String className = "testClass";

        Object object = taskDataProvider.lookup(className, "lookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForEncounter() {
        String className = Encounter.class.getSimpleName();

        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(encounterService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(encounterService.getEncounterByUuid(null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid(null);
    }

    @Test
    public void shouldReturnNullWhenEncounterNotFoundForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(encounterService.getEncounterByUuid("4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(encounterService).getEncounterByUuid("4");
    }

    @Test
    public void shouldReturnEncounterForLookupGetEncounterByUuid() {
        String className = Encounter.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Encounter encounter = new Encounter();
        encounter.setEncounterType(new EncounterType("encounterTypeTest"));

        when(encounterService.getEncounterByUuid("5")).thenReturn(encounter);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertEquals(encounter, object);
        verify(encounterService).getEncounterByUuid("5");
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForPatient() {
        String className = Patient.class.getSimpleName();

        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(patientService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByUuid(null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid(null);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(patientService.getPatientByMotechId(null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId(null);
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(patientService.getPatientByUuid("4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByUuid("4");
    }

    @Test
    public void shouldReturnNullWhenPatientNotFoundForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, "4");

        when(patientService.getPatientByMotechId("4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_MOTECH_ID, lookupFields);

        assertNull(object);
        verify(patientService).getPatientByMotechId("4");
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByUuid() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Patient patient = new Patient();
        patient.setMotechId("10");

        when(patientService.getPatientByUuid("5")).thenReturn(patient);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByUuid("5");
    }

    @Test
    public void shouldReturnPatientForLookupGetPatientByMotechId() {
        String className = Patient.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(MOTECH_ID, "5");

        Patient patient = new Patient();
        patient.setUuid("10");

        when(patientService.getPatientByMotechId("5")).thenReturn(patient);

        Object object = taskDataProvider.lookup(className, BY_MOTECH_ID, lookupFields);

        assertEquals(patient, object);
        verify(patientService).getPatientByMotechId("5");
    }

    @Test
    public void shouldReturnNullWhenWrongLookupNameForProvider() {
        String className = Provider.class.getSimpleName();

        Object object = taskDataProvider.lookup(className, "wrongLookupName", null);

        assertNull(object);
        verifyZeroInteractions(providerService);
    }

    @Test
    public void shouldReturnNullWhenEmptyLookupFieldsForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();

        when(providerService.getProviderByUuid(null)).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid(null);
    }

    @Test
    public void shouldReturnNullWhenProviderNotFoundForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "4");

        when(providerService.getProviderByUuid("4")).thenReturn(null);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertNull(object);
        verify(providerService).getProviderByUuid("4");
    }

    @Test
    public void shouldReturnProviderForLookupGetProviderByUuid() {
        String className = Provider.class.getSimpleName();
        Map<String, String> lookupFields = new HashMap<>();
        lookupFields.put(UUID, "5");

        Provider provider = new Provider();
        provider.setIdentifier("testIdentifier");

        when(providerService.getProviderByUuid("5")).thenReturn(provider);

        Object object = taskDataProvider.lookup(className, BY_UUID, lookupFields);

        assertEquals(provider, object);
        verify(providerService).getProviderByUuid("5");
    }
}