package com.digitalhealthsolutions.hapi;

import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;

/**
 * Test class for printing patient identifiers.
 * 
 * @author Steve McKee
 */
public class HapiTest {
	
	String fhirBaseUrl = "";
	String accessToken = "";
	String patientFhirId = "";
	
	public void printIdentifiers() {
		
		// Create the context and client for STU3
		FhirContext fhirContext = FhirContext.forDstu3();
		IGenericClient client = fhirContext.newRestfulGenericClient(this.fhirBaseUrl);
		
		// Register an authenticator containing the access token
		BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(this.accessToken);
		client.registerInterceptor(authInterceptor);
		
		// Retrieve the Patient resource
		Patient patient = client.read().resource(Patient.class).withId(this.patientFhirId).execute();
		
		// Loop through and print the patient identifiers
		List<Identifier> patientIdentifiers = patient.getIdentifier();
		for (int i = 0; i < patientIdentifiers.size(); i++) {
			Identifier patientIdentifier = patientIdentifiers.get(i);
			IdentifierUse use = patientIdentifier.getUse();
			String system = patientIdentifier.getSystem();
			String value = patientIdentifier.getValue();
			CodeableConcept type = patientIdentifier.getType();
			
			System.out.println("Patient identifer #" + (i + 1));
			System.out.println("\tuse: " + ((use != null) ? use.toString() : ""));
			System.out.println("\ttype: " + ((type != null) ? type.getText() : ""));
			System.out.println("\tsystem: " + system);
			System.out.println("\tvalue: " + value);
			System.out.println("\n");
		}
	}
	
	/**
	 * Main
	 * 
	 * @param args Arguments for the application
	 */
	public static void main(String[] args) {
		try {
			HapiTest test = new HapiTest();
			test.printIdentifiers();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			System.exit(0);
		}
	}
	
}
