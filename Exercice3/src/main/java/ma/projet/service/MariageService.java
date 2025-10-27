package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
@Transactional
public class MariageService implements IDao<Mariage> {

    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public boolean create(Mariage o) {
        Session session = sessionFactory.getCurrentSession();
        session.save(o);
        return true;
    }

    @Override
    public boolean delete(Mariage o) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(o);
        return true;
    }

    @Override
    public boolean update(Mariage o) {

        Session session = sessionFactory.getCurrentSession();
        session.update(o);
        return true;
    }

    @Override
    public Mariage findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Mariage.class, id);
    }

    @Override
    public List<Mariage> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Mariage", Mariage.class).getResultList();
    }


}
