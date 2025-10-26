package ma.projet.service;

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
public class TacheService implements IDao<Tache> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Tache o) {
        try {
            sessionFactory.getCurrentSession().save(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Tache o) {
        try {
            sessionFactory.getCurrentSession().delete(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Tache o) {
        try {
            sessionFactory.getCurrentSession().update(o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Tache findById(int id) {
        try {
            return sessionFactory.getCurrentSession().get(Tache.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Tache> findAll() {
        try {
            return sessionFactory.getCurrentSession().createQuery("from Tache", Tache.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Tache> findByProjet(int projetId) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery("from Tache where projet.id = :id", Tache.class)
                    .setParameter("id", projetId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Affiche les tâches dont le prix est supérieur à 1000 DH (requête nommée)
     */
    public void afficherTachesPrixSuperieur() {
        try {
            double prixMinimum = 1000.0;

            System.out.println("Tâches dont le prix est supérieur à " + prixMinimum + " DH:");
            System.out.println("Num Nom            Projet              Prix");

            List<Tache> taches = sessionFactory.getCurrentSession()
                    .createNamedQuery("Tache.findByPrixSuperieur", Tache.class)
                    .setParameter("prix", prixMinimum)
                    .list();

            for (Tache tache : taches) {
                String projetNom = tache.getProjet() != null ? tache.getProjet().getNom() : "Non défini";

                System.out.printf("%-3d %-15s %-20s %.2f DH%n", 
                        tache.getId(), 
                        tache.getNom(), 
                        projetNom,
                        tache.getPrix());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche les tâches réalisées entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     */
    public void afficherTachesRealiseesPeriode(Date dateDebut, Date dateFin) {
        try {
            if (dateDebut == null || dateFin == null) {
                System.out.println("Les dates de début et de fin sont requises");
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            System.out.println("Tâches réalisées entre " + dateFormat.format(dateDebut) + " et " + dateFormat.format(dateFin) + ":");
            System.out.println("Num Nom            Projet              Date Début Réelle   Date Fin Réelle");

            List<Object[]> tachesRealisees = sessionFactory.getCurrentSession()
                    .createQuery("select t, et from Tache t join t.employeTaches et " +
                                 "where et.dateFinReelle is not null " +
                                 "and et.dateFinReelle between :dateDebut and :dateFin", Object[].class)
                    .setParameter("dateDebut", dateDebut)
                    .setParameter("dateFin", dateFin)
                    .list();

            for (Object[] result : tachesRealisees) {
                Tache tache = (Tache) result[0];
                EmployeTache et = (EmployeTache) result[1];

                String projetNom = tache.getProjet() != null ? tache.getProjet().getNom() : "Non défini";
                String dateDebutReelle = et.getDateDebutReelle() != null ? dateFormat.format(et.getDateDebutReelle()) : "Non définie";
                String dateFinReelle = et.getDateFinReelle() != null ? dateFormat.format(et.getDateFinReelle()) : "Non définie";

                System.out.printf("%-3d %-15s %-20s %-20s %-20s%n", 
                        tache.getId(), 
                        tache.getNom(), 
                        projetNom,
                        dateDebutReelle, 
                        dateFinReelle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
