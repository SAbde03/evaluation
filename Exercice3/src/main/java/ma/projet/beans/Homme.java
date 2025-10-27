package ma.projet.beans;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Homme extends Personne {

    @OneToMany(mappedBy="homme")
    private List<Mariage> mariages;

    public Homme() {
        super();
    }

    public Homme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        // Calls the five-argument constructor of the Personne class
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    public void setMariages(List<Mariage> mariages) {
        this.mariages = mariages;
    }

}
