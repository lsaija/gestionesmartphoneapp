package it.prova.gestionesmartphoneapp.dao.smartphone;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import it.prova.gestionesmartphoneapp.model.Smartphone;

public class SmartphoneDAOImpl implements SmartphoneDAO {

	private EntityManager entityManager;

	@Override
	public List<Smartphone> list() throws Exception {
		return entityManager.createQuery("from Smartphone", Smartphone.class).getResultList();
	}

	@Override
	public Smartphone get(Long id) throws Exception {
		return entityManager.find(Smartphone.class, id);
	}

	@Override
	public void update(Smartphone input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		input = entityManager.merge(input);
	}

	@Override
	public void insert(Smartphone input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.persist(input);
	}

	@Override
	public void delete(Smartphone input) throws Exception {
		if (input == null) {
			throw new Exception("Problema valore in input");
		}
		entityManager.remove(entityManager.merge(input));
	}

	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Smartphone findByIdFetchingApps(Long id) throws Exception {
		TypedQuery<Smartphone> query = entityManager.createQuery(
				"select s FROM Smartphone s left join fetch s.apps a where s.id = :idSmartphone", Smartphone.class);
		query.setParameter("idSmartphone", id);
		return query.getResultStream().findFirst().orElseGet(null);
	}

	@Override
	public void unstallAppFromSmartphone(Long idApp) throws Exception {
		entityManager.createNativeQuery("delete from smartphone_app where app_id=?1").setParameter(1, idApp)
				.executeUpdate();
		this.delete(this.get(idApp));
	}

}
