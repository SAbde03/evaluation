package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class HommeService implements IDao<Homme> {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public boolean create(Homme o) {
        Session s = currentSession();
        s.save(o);
        return true;
    }

    @Override
    public boolean delete(Homme o) {
        Session s = currentSession();
        s.delete(o);
        return true;
    }

    @Override
    public boolean update(Homme o) {
        Session s = currentSession();
        s.update(o);
        return true;
    }

    @Override
    public Homme findById(int id) {
        return currentSession().get(Homme.class, id);
    }

    @Override
    public List<Homme> findAll() {
        return currentSession().createQuery("from Homme", Homme.class).getResultList();
    }

    public List<Femme> findEpousesBetweenDates(Homme homme, Date startDate, Date endDate) {

        String hql = "SELECT m.femme FROM Mariage m WHERE m.homme = :h AND m.dateDebut BETWEEN :start AND :end";

        return currentSession().createQuery(hql, Femme.class)
                .setParameter("h", homme)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList();
    }


    public Long countMenMarriedToFourWomenBetweenDates(Date startDate, Date endDate) {
        Session session = currentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<Mariage> mariageRoot = cq.from(Mariage.class);

        Predicate datePredicate = cb.between(mariageRoot.get("dateDebut"), startDate, endDate);


        CriteriaQuery<Homme> subQuery = cb.createQuery(Homme.class);
        Root<Mariage> subRoot = subQuery.from(Mariage.class);

        subQuery.select(subRoot.get("homme"))
                .where(cb.between(subRoot.get("dateDebut"), startDate, endDate))
                .groupBy(subRoot.get("homme"))
                .having(cb.equal(cb.countDistinct(subRoot.get("femme")), 4L));


        List<Homme> hommesConcernés = session.createQuery(subQuery).getResultList();

        return (long) hommesConcernés.size();
    }

    public void displayMariagesDetails(int hommeId) {
        Homme homme = findById(hommeId);

        if (homme == null) {
            System.out.println("Erreur : Homme non trouvé avec l'ID " + hommeId);
            return;
        }

        Hibernate.initialize(homme.getMariages());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Nom : " + homme.getPrenom() + " " + homme.getNom());

        List<Mariage> ongoing = homme.getMariages().stream()
                .filter(m -> m.getDateFin() == null)
                .collect(Collectors.toList());

        List<Mariage> failed = homme.getMariages().stream()
                .filter(m -> m.getDateFin() != null)
                .collect(Collectors.toList());

        System.out.println("Mariages En Cours :");
        if (ongoing.isEmpty()) {
            System.out.println("(Aucun mariage en cours)");
        } else {
            for (int i = 0; i < ongoing.size(); i++) {
                Mariage m = ongoing.get(i);
                Femme femme = m.getFemme();
                System.out.printf("%d. Femme : %s %s   Date Début : %s    Nbr Enfants : %d\n",
                        i + 1, femme.getPrenom(), femme.getNom(), df.format(m.getDateDebut()), m.getNbrEnfant());
            }
        }

        System.out.println("\nMariages échoués :");
        if (failed.isEmpty()) {
            System.out.println("(Aucun mariage échoué)");
        } else {
            for (int i = 0; i < failed.size(); i++) {
                Mariage m = failed.get(i);
                Femme femme = m.getFemme();
                System.out.printf("%d. Femme : %s %s  Date Début : %s    \nDate Fin : %s    Nbr Enfants : %d\n",
                        i + 1, femme.getPrenom(), femme.getNom(), df.format(m.getDateDebut()),
                        df.format(m.getDateFin()), m.getNbrEnfant());
            }
        }
    }

}
