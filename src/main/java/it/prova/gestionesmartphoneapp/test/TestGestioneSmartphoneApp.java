package it.prova.gestionesmartphoneapp.test;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import it.prova.gestionesmartphoneapp.dao.EntityManagerUtil;
import it.prova.gestionesmartphoneapp.model.App;
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
			System.out.println(
					"In tabella Smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"**************************** inizio batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");

			testInserimentoNuovoSmartphone(smartphoneServiceInstance);

			testAggiornamentoVersioneOS(smartphoneServiceInstance);

			testInserimentoNuovaApp(appServiceInstance);

			testAggiornamentoVersioneAppConData(appServiceInstance);

			testInstallaAppSuSmartphone(smartphoneServiceInstance, appServiceInstance);

			testRimuoviSmartphoneConApp(smartphoneServiceInstance, appServiceInstance);
			System.out.println(
					"****************************** fine batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");

			System.out.println("In tabella App ci sono " + appServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"In tabella Smartphone ci sono " + smartphoneServiceInstance.listAll().size() + " elementi.");

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
			throw new RuntimeException(
					"testAggiornamentoVersioneOS fallito: le date di aggiornamento sono disallineate ");

		if (!smartphoneInstance.getCreateDateTime().equals(createDateTimeIniziale))
			throw new RuntimeException("testAggiornamentoVersioneOS fallito: la data di creazione è cambiata ");

		System.out.println(".......testAggiornamentoVersioneOS fine: PASSED.............");
	}

	private static void testInserimentoNuovaApp(AppService appServiceInstance) throws Exception {
		System.out.println(".......testInserimentoNuovaApp inizio.............");

		App appInstance = new App("nome1", new SimpleDateFormat("dd/MM/yyyy").parse("10/08/2021"), "versione1");
		appServiceInstance.inserisciNuovo(appInstance);
		if (appInstance.getId() == null)
			throw new RuntimeException("testInserimentoNuovaApp fallito ");

		System.out.println(".......testInserimentoNuovaApp fine: PASSED.............");
	}

	private static void testAggiornamentoVersioneAppConData(AppService appServiceInstance) throws Exception {
		System.out.println(".......testAggiornamentoVersioneAppConData inizio.............");

		App appInstance = new App("nome1", new SimpleDateFormat("dd/MM/yyyy").parse("10/08/2021"), "versione1");
		appServiceInstance.inserisciNuovo(appInstance);
		if (appInstance.getId() == null)
			throw new RuntimeException("testAggiornamentoVersioneAppConData fallito ");

		LocalDateTime createDateTimeIniziale = appInstance.getCreateDateTime();
		LocalDateTime updateDateTimeIniziale = appInstance.getUpdateDateTime();

		appInstance.setVersione("versione2");
		appServiceInstance.aggiorna(appInstance);
		Instant instant = appInstance.getUpdateDateTime().toInstant(ZoneOffset.UTC);
		Date dataAggiornata = Date.from(instant);
		appInstance.setDataUltimoAggiornamento(dataAggiornata);
		appServiceInstance.aggiorna(appInstance);

		if (appInstance.getUpdateDateTime().isAfter(updateDateTimeIniziale))
			throw new RuntimeException(
					"testAggiornamentoVersioneAppConData fallito: le date di aggiornamento sono disallineate ");

		if (!appInstance.getCreateDateTime().equals(createDateTimeIniziale))
			throw new RuntimeException("testAggiornamentoVersioneAppConData fallito: la data di creazione è cambiata ");

		System.out.println(".......testAggiornamentoVersioneAppConData fine: PASSED.............");
	}

	private static void testInstallaAppSuSmartphone(SmartphoneService smartphoneServiceInstance,
			AppService appServiceInstance) throws Exception {
		System.out.println(".......testInstallaAppSuSmartphone inizio.............");

		Smartphone smartphoneInstance = new Smartphone("marca1", "modello1", 300, "versioneOS1");
		smartphoneServiceInstance.inserisciNuovo(smartphoneInstance);

		if (smartphoneInstance.getId() == null)
			throw new RuntimeException("testInstallaAppSuSmartphone fallito: inserimento Smartphone non riuscito ");

		App appInstance = new App("lollogram", new SimpleDateFormat("dd/MM/yyyy").parse("10/08/2021"),
				"versione.0.0.1");
		appServiceInstance.inserisciNuovo(appInstance);

		if (appInstance.getId() == null)
			throw new RuntimeException("testInstallaAppSuSmartphone fallito: app non inserita ");

		smartphoneServiceInstance.aggiungiApp(smartphoneInstance, appInstance);

		Instant instant = appInstance.getUpdateDateTime().toInstant(ZoneOffset.UTC);
		Date dataAggiornata = Date.from(instant);
		appInstance.setDataInstallazione(dataAggiornata);
		appServiceInstance.aggiorna(appInstance);

		Smartphone smartphoneReloaded = smartphoneServiceInstance
				.caricaSingoloElementoEagerApps(smartphoneInstance.getId());
		if (smartphoneReloaded.getApps().isEmpty())
			throw new RuntimeException("testInstallaAppSuSmartphone fallito: app non installata ");

		System.out.println(".......testInstallaAppSuSmartphone fine: PASSED.............");

		System.out.println(".......testDisinstallazioneAppSuSmartphone inizio.............");

		smartphoneServiceInstance.rimuoviApp(smartphoneInstance, appInstance);
		Smartphone smartphoneReloaded2 = smartphoneServiceInstance
				.caricaSingoloElementoEagerApps(smartphoneInstance.getId());
		if (!smartphoneReloaded2.getApps().isEmpty())
			throw new RuntimeException("testDisinstallazioneAppSuSmartphone fallito: app non installata ");

		System.out.println(".......testDisinstallazioneAppSuSmartphone fine: PASSED.............");

	}

	private static void testRimuoviSmartphoneConApp(SmartphoneService smartphoneServiceInstance,
			AppService appServiceInstance) throws Exception {
		System.out.println(".......testRimuoviSmartphoneConApp inizio.............");

		Smartphone smartphoneInstance = new Smartphone("marca1", "modello1", 300, "versioneOS1");
		smartphoneServiceInstance.inserisciNuovo(smartphoneInstance);

		if (smartphoneInstance.getId() == null)
			throw new RuntimeException("testRimuoviSmartphoneConApp fallito: inserimento Smartphone non riuscito ");

		App appInstance1 = new App("lollogram", new SimpleDateFormat("dd/MM/yyyy").parse("10/08/2021"),
				"versione.0.0.1");
		appServiceInstance.inserisciNuovo(appInstance1);
		App appInstance2 = new App("lollobook", new SimpleDateFormat("dd/MM/yyyy").parse("10/08/2021"),
				"versione.0.0.1");
		appServiceInstance.inserisciNuovo(appInstance2);
		if (appInstance1.getId() == null)
			throw new RuntimeException("testRimuoviSmartphoneConApp fallito: app non inserita ");
		if (appInstance2.getId() == null)
			throw new RuntimeException("testRimuoviSmartphoneConApp fallito: app non inserita ");

		smartphoneServiceInstance.aggiungiApp(smartphoneInstance, appInstance1);
		smartphoneServiceInstance.aggiungiApp(smartphoneInstance, appInstance2);
		Smartphone smartphoneReloaded = smartphoneServiceInstance
				.caricaSingoloElementoEagerApps(smartphoneInstance.getId());
		if (smartphoneReloaded.getApps().isEmpty())
			throw new RuntimeException("testRimuoviSmartphoneConApp fallito: app non installata ");

		smartphoneServiceInstance.rimuovi(smartphoneInstance.getId());

		System.out.println(".......testRimuoviSmartphoneConApp fine: PASSED.............");

	}

}
