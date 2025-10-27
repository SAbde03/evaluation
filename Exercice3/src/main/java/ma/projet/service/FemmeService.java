package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional

public class FemmeService implements IDao<Femme> {

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public boolean create(Femme o) {
        Session session = sessionFactory.getCurrentSession();
        session.save(o);
        return true;
    }

    @Override
    public boolean delete(Femme o) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(o);
        return true;
    }

    @Override
    public boolean update(Femme o) {

        Session session = sessionFactory.getCurrentSession();
        session.update(o);
        return true;
    }

    @Override
    public Femme findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Femme.class, id);
    }

    @Override
    public List<Femme> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Femme", Femme.class).getResultList();
    }

    public Long countEnfantsBetweenDates(int femmeId, Date startDate, Date endDate) {
        NativeQuery query = sessionFactory.getCurrentSession().getNamedNativeQuery("Femme.countEnfantsBetweenDates");
        query.setParameter("femmeId", femmeId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        Number result = (Number) query.uniqueResult();
        return (result != null) ? result.longValue() : 0L;
    }


    public List<Femme> findMarriedAtLeastTwice() {
        Query<Femme> query = sessionFactory.getCurrentSession().createNamedQuery("Femme.marriedAtLeastTwice", Femme.class);
        return query.getResultList();
    }

    public Femme findOldestFemme() {
        return sessionFactory.getCurrentSession().createQuery("FROM Femme f ORDER BY f.dateNaissance ASC", Femme.class)
                .setMaxResults(1)
                .uniqueResult();
    }
}
