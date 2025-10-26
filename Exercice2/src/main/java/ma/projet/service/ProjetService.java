package ma.projet.service;

import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class ProjetService implements IDao<Projet> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Projet o) {
        try {
            sessionFactory.getCurrentSession().save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Projet o) {
        try {
            sessionFactory.getCurrentSession().delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Projet o) {
        try {
            sessionFactory.getCurrentSession().update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Projet findById(int id) {
        try {
            return sessionFactory.getCurrentSession().get(Projet.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Projet> findAll() {
        try {
            return sessionFactory.getCurrentSession().createQuery("from Projet", Projet.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Affiche la liste des tâches planifiées pour un projet
     * @param projetId ID du projet
     */
    public void afficherTachesPlanifiees(int projetId) {
        try {
            Projet projet = findById(projetId);
            if (projet == null) {
                System.out.println("Projet non trouvé");
                return;
            }

            System.out.println("Tâches planifiées pour le projet : " + projet.getNom());
            System.out.println("Num Nom            Date Début        Date Fin          Prix");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            List<Tache> taches = sessionFactory.getCurrentSession()
                    .createQuery("from Tache where projet.id = :id", Tache.class)
                    .setParameter("id", projetId)
                    .list();

            for (Tache tache : taches) {
                String dateDebut = tache.getDateDebut() != null ? dateFormat.format(tache.getDateDebut()) : "Non définie";
                String dateFin = tache.getDateFin() != null ? dateFormat.format(tache.getDateFin()) : "Non définie";

                System.out.printf("%-3d %-15s %-17s %-17s %.2f DH%n", 
                        tache.getId(), 
                        tache.getNom(), 
                        dateDebut, 
                        dateFin,
                        tache.getPrix());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la liste des tâches réalisées avec les dates réelles
     * @param projetId ID du projet
     */
    public void afficherTachesRealisees(int projetId) {
        try {
            Projet projet = findById(projetId);
            if (projet == null) {
                System.out.println("Projet non trouvé");
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat monthFormat = new SimpleDateFormat("dd MMMM yyyy");

            String dateDebutFormatted = projet.getDateDebut() != null ? monthFormat.format(projet.getDateDebut()) : "Non définie";

            System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom() + "     Date début : " + dateDebutFormatted);
            System.out.println("Liste des tâches:");
            System.out.println("Num Nom            Date Début Réelle   Date Fin Réelle");

            // Récupérer les tâches du projet qui ont été réalisées (avec date de fin réelle non nulle)
            List<Object[]> tachesRealisees = sessionFactory.getCurrentSession()
                    .createQuery("select t, et from Tache t join t.employeTaches et " +
                                 "where t.projet.id = :id and et.dateFinReelle is not null", Object[].class)
                    .setParameter("id", projetId)
                    .list();

            for (Object[] result : tachesRealisees) {
                Tache tache = (Tache) result[0];
                EmployeTache et = (EmployeTache) result[1];

                String dateDebutReelle = et.getDateDebutReelle() != null ? dateFormat.format(et.getDateDebutReelle()) : "Non définie";
                String dateFinReelle = et.getDateFinReelle() != null ? dateFormat.format(et.getDateFinReelle()) : "Non définie";

                System.out.printf("%-3d %-15s %-20s %-20s%n", 
                        tache.getId(), 
                        tache.getNom(), 
                        dateDebutReelle, 
                        dateFinReelle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
