package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class EmployeTacheService implements IDao<EmployeTache> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(EmployeTache o) {
        try {
            sessionFactory.getCurrentSession().save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(EmployeTache o) {
        try {
            sessionFactory.getCurrentSession().delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(EmployeTache o) {
        try {
            sessionFactory.getCurrentSession().update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public EmployeTache findById(int id) {
        try {
            return sessionFactory.getCurrentSession().get(EmployeTache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<EmployeTache> findAll() {
        try {
            return sessionFactory.getCurrentSession().createQuery("from EmployeTache", EmployeTache.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EmployeTache> findByEmploye(int employeId) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("from EmployeTache where employe.id = :id", EmployeTache.class)
                    .setParameter("id", employeId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<EmployeTache> findByTache(int tacheId) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("from EmployeTache where tache.id = :id", EmployeTache.class)
                    .setParameter("id", tacheId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
