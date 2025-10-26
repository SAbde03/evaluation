package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

@Repository
@Transactional
public class EmployeService implements IDao<Employe> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Employe o) {
        try {
            sessionFactory.getCurrentSession().save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Employe o) {
        try {
            sessionFactory.getCurrentSession().delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Employe o) {
        try {
            sessionFactory.getCurrentSession().update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Employe findById(int id) {
        try {
            return sessionFactory.getCurrentSession().get(Employe.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Employe> findAll() {
        try {
            return sessionFactory.getCurrentSession().createQuery("from Employe", Employe.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Employe> findByProjet(int projetId) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("select distinct e from Employe e join e.employeTaches et join et.tache t where t.projet.id = :id", Employe.class)
                    .setParameter("id", projetId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Affiche la liste des tâches réalisées par un employé
     * @param employeId ID de l'employé
     */
    public void afficherTachesRealisees(int employeId) {
        try {
            Employe employe = findById(employeId);
            if (employe == null) {
                System.out.println("Employé non trouvé");
                return;
            }

            System.out.println("Tâches réalisées par l'employé : " + employe.getNom() + " " + employe.getPrenom());
            System.out.println("Num Nom            Date Début Réelle   Date Fin Réelle");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            List<EmployeTache> employeTaches = sessionFactory.getCurrentSession()
                    .createQuery("from EmployeTache where employe.id = :id and dateFinReelle is not null", EmployeTache.class)
                    .setParameter("id", employeId)
                    .list();

            for (EmployeTache et : employeTaches) {
                Tache tache = et.getTache();
                String dateDebut = et.getDateDebutReelle() != null ? dateFormat.format(et.getDateDebutReelle()) : "Non définie";
                String dateFin = et.getDateFinReelle() != null ? dateFormat.format(et.getDateFinReelle()) : "Non définie";

                System.out.printf("%-3d %-15s %-20s %-20s%n", 
                        tache.getId(), 
                        tache.getNom(), 
                        dateDebut, 
                        dateFin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la liste des projets gérés par un employé
     * @param employeId ID de l'employé
     */
    public void afficherProjetsGeres(int employeId) {
        try {
            Employe employe = findById(employeId);
            if (employe == null) {
                System.out.println("Employé non trouvé");
                return;
            }

            System.out.println("Projets gérés par l'employé : " + employe.getNom() + " " + employe.getPrenom());
            System.out.println("Num Nom                 Date Début        Date Fin");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            List<Projet> projets = sessionFactory.getCurrentSession()
                    .createQuery("from Projet where chef.id = :id", Projet.class)
                    .setParameter("id", employeId)
                    .list();

            for (Projet projet : projets) {
                String dateDebut = projet.getDateDebut() != null ? dateFormat.format(projet.getDateDebut()) : "Non définie";
                String dateFin = projet.getDateFin() != null ? dateFormat.format(projet.getDateFin()) : "Non définie";

                System.out.printf("%-3d %-20s %-17s %-17s%n", 
                        projet.getId(), 
                        projet.getNom(), 
                        dateDebut, 
                        dateFin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
