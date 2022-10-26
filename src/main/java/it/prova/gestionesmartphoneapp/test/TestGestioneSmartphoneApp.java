package it.prova.gestionesmartphoneapp.test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import it.prova.gestionesmartphoneapp.dao.EntityManagerUtil;
import it.prova.gestionesmartphoneapp.model.Smartphone;
import it.prova.gestionesmartphoneapp.service.AppService;
import it.prova.gestionesmartphoneapp.service.MyServiceFactory;
import it.prova.gestionesmartphoneapp.service.SmartphoneService;


public class TestGestioneSmartphoneApp {

	public static void main(String[] args) {
		SmartphoneService smartphoneServiceInstance = MyServiceFactory.getSmartphoneServiceInstance();
		AppService appServiceInstance = MyServiceFactory.getAppServiceInstance();

		try {

			System.out.println("In tabella App ci sono " + appServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"**************************** inizio batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");

			testInserimentoNuovoSmartphone(smartphoneServiceInstance);
			
			testAggiornamentoVersioneOS(smartphoneServiceInstance);
			
			
			
			
			System.out.println(
					"****************************** fine batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");
			
			System.out.println("In tabella App ci sono " + appServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");

		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			EntityManagerUtil.shutdown();
		}
	}

	private static void testInserimentoNuovoSmartphone(SmartphoneService smartphoneServiceInstance) throws Exception {
		System.out.println(".......testInserimentoNuovoSmartphone inizio.............");

		Smartphone smartphoneInstance = new Smartphone("marca1", "modello1", 300, "versioneOS1");
		smartphoneServiceInstance.inserisciNuovo(smartphoneInstance);
		if (smartphoneInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovoSmartphone fallito ");

		System.out.println(".......testInserimentoNuovoSmartphone fine: PASSED.............");
	}
	
	private static void testAggiornamentoVersioneOS(SmartphoneService smartphoneServiceInstance) throws Exception {
		System.out.println(".......testAggiornamentoVersioneOS inizio.............");

		Smartphone smartphoneInstance = new Smartphone("marca1", "modello1", 300, "versioneOS1");
		smartphoneServiceInstance.inserisciNuovo(smartphoneInstance);
		if (smartphoneInstance.getId() == null)
			throw new RuntimeException("testModificaECheckDateCd fallito ");
		
		LocalDateTime createDateTimeIniziale = smartphoneInstance.getCreateDateTime();
		LocalDateTime updateDateTimeIniziale = smartphoneInstance.getUpdateDateTime();

		smartphoneInstance.setVersioneOS("versioneOS2");
		smartphoneServiceInstance.aggiorna(smartphoneInstance);

		if (smartphoneInstance.getUpdateDateTime().isAfter(updateDateTimeIniziale))
				throw new RuntimeException("testAggiornamentoVersioneOS fallito: le date di aggiornamento sono disallineate ");

		if (!smartphoneInstance.getCreateDateTime().equals(createDateTimeIniziale))
			    throw new RuntimeException("testAggiornamentoVersioneOS fallito: la data di creazione è cambiata ");

		System.out.println(".......testAggiornamentoVersioneOS fine: PASSED.............");
		}

}
