package ma.projet.beans;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@NamedNativeQuery(
        name = "Femme.countEnfantsBetweenDates",
        query = "SELECT IFNULL(SUM(m.nbrEnfant), 0) FROM Mariage m WHERE m.femme_id = :femmeId AND m.dateDebut BETWEEN :startDate AND :endDate"
)
@NamedQuery(
        name = "Femme.marriedAtLeastTwice",
        query = "SELECT f FROM Femme f JOIN f.mariages m GROUP BY f HAVING COUNT(m) >= 2"
)
@Entity
public class Femme extends Personne {

    @OneToMany(mappedBy="femme")
    private List<Mariage> mariages;

    public Femme() {
        super();
    }

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        // Calls the five-argument constructor of the Personne class
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }

    @Override
    public String toString() {
        return "Femme{" +
                "mariages=" + mariages +
                ", dateNaissance=" + dateNaissance +
                ", adresse='" + adresse + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }
}
