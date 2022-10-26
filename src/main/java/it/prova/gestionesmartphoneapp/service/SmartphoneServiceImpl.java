package it.prova.gestionesmartphoneapp.service;

import java.util.List;

import javax.persistence.EntityManager;

import it.prova.gestionesmartphoneapp.dao.EntityManagerUtil;
import it.prova.gestionesmartphoneapp.dao.smartphone.SmartphoneDAO;
import it.prova.gestionesmartphoneapp.model.Smartphone;




public class SmartphoneServiceImpl implements SmartphoneService {
	
	private SmartphoneDAO smartphoneDAO;

	@Override
	public void setSmartphoneDAO(SmartphoneDAO smartphoneDAO) {
		this.smartphoneDAO = smartphoneDAO;
	}

	@Override
	public List<Smartphone> listAll() throws Exception {
		
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			
			smartphoneDAO.setEntityManager(entityManager);

			
			return smartphoneDAO.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public Smartphone caricaSingoloElemento(Long id) throws Exception {
		
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			
			smartphoneDAO.setEntityManager(entityManager);

			return smartphoneDAO.get(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}
	
	@Override
	public void aggiorna(Smartphone smartphoneInstance) throws Exception {
		
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			
			entityManager.getTransaction().begin();

			smartphoneDAO.setEntityManager(entityManager);

			smartphoneDAO.update(smartphoneInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void inserisciNuovo(Smartphone smartphoneInstance) throws Exception {
		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			
			entityManager.getTransaction().begin();

			smartphoneDAO.setEntityManager(entityManager);

			smartphoneDAO.insert(smartphoneInstance);

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public void rimuovi(Long idSmartphone) throws Exception {

		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			entityManager.getTransaction().begin();

			
			smartphoneDAO.setEntityManager(entityManager);

			smartphoneDAO.delete(smartphoneDAO.get(idSmartphone));

			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

	@Override
	public Smartphone caricaSingoloElementoEagerApps(Long id) throws Exception {

		EntityManager entityManager = EntityManagerUtil.getEntityManager();

		try {
			smartphoneDAO.setEntityManager(entityManager);
			return smartphoneDAO.findByIdFetchingApps(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			EntityManagerUtil.closeEntityManager(entityManager);
		}
	}

}
