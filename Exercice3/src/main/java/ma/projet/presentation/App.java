package ma.projet.presentation;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
@Component
public class App {

    @Autowired private HommeService hs;
    @Autowired private FemmeService fs;
    @Autowired private MariageService ms;

    public void run() {

        Date date1989 = new Date(89, 8, 3);
        Date date1990 = new Date(90, 8, 3);
        Date date1995 = new Date(95, 8, 3);
        Date date2000 = new Date(100, 0, 1);
        Date date2005 = new Date(105, 0, 1);


        System.out.println("--- 1. Création des Hommes et Femmes ---");

        Homme h1_said = new Homme("SAFI", "SAID", "0600112233", "Rabat", new Date(70, 0, 1));
        Homme h2_yassine = new Homme("ALAMI", "YASSINE", "0600112244", "Sale", new Date(75, 5, 5));
        Homme h3_kamal = new Homme("JABRI", "KAMAL", "0600112255", "Fes", new Date(80, 10, 10));
        Homme h4_mohamed = new Homme("ALI", "MOHAMED", "0600112266", "Casa", new Date(85, 2, 20));
        Homme h5_anas = new Homme("RAMI", "ANAS", "0600112277", "Tanger", new Date(90, 8, 1));

        hs.create(h1_said); hs.create(h2_yassine); hs.create(h3_kamal); hs.create(h4_mohamed); hs.create(h5_anas);

        Femme f1_salima = new Femme("RAMI", "SALIMA", "0611223344", "Rabat", new Date(68, 8, 3));
        Femme f2_amal = new Femme("ALI", "AMAL", "0611223355", "Sale", new Date(72, 1, 15));
        Femme f3_wafa = new Femme("ALAOUI", "WAFA", "0611223366", "Fes", new Date(78, 6, 25));
        Femme f4_karima = new Femme("ALAMI", "KARIMA", "0611223377", "Casa", new Date(82, 3, 5));
        Femme f5_lina = new Femme("HASSANI", "LINA", "0611223388", "Tanger", new Date(87, 9, 12));
        Femme f6_sara = new Femme("IDRISSI", "SARA", "0611223399", "Marrakech", new Date(92, 4, 18));
        Femme f7_zineb = new Femme("BENANI", "ZINEB", "0611223400", "Agadir", new Date(95, 11, 2));
        Femme f8_mariam = new Femme("JILALI", "MARIAM", "0611223411", "Oujda", new Date(98, 7, 7));
        Femme f9_ayman = new Femme("FAHMI", "AYMAN", "0611223422", "Kénitra", new Date(85, 0, 1));
        Femme f10_najat = new Femme("BAHRI", "NAJAT", "0611223433", "Safi", new Date(88, 5, 5));

        fs.create(f1_salima); fs.create(f2_amal); fs.create(f3_wafa); fs.create(f4_karima); fs.create(f5_lina);
        fs.create(f6_sara); fs.create(f7_zineb); fs.create(f8_mariam); fs.create(f9_ayman); fs.create(f10_najat);

        // --- Création des Mariages pour les tests ---
        ms.create(new Mariage(date1989, date1990, 0, h1_said, f4_karima));
        ms.create(new Mariage(date1990, null, 4, h1_said, f1_salima));
        ms.create(new Mariage(date1995, null, 2, h1_said, f2_amal));      
        ms.create(new Mariage(date2000, null, 3, h1_said, f3_wafa));
        ms.create(new Mariage(new Date(2001, 1, 1), null, 1, h2_yassine, f5_lina));
        ms.create(new Mariage(new Date(2002, 1, 1), null, 1, h2_yassine, f6_sara));
        ms.create(new Mariage(new Date(2003, 1, 1), null, 1, h2_yassine, f7_zineb));
        ms.create(new Mariage(new Date(2004, 1, 1), null, 1, h2_yassine, f8_mariam));
        ms.create(new Mariage(new Date(2012, 1, 1), null, 1, h3_kamal, f9_ayman));
        ms.create(new Mariage(new Date(2018, 1, 1), null, 0, h5_anas, f10_najat));

        System.out.println("------------------------------------------------------------------");

        // --- 2. Afficher la liste des femmes ---
        System.out.println("\n--- 2. Liste de toutes les femmes ---");
        fs.findAll().forEach(f -> System.out.println("ID: " + f.getId() + " - Nom: " + f.getPrenom() + " " + f.getNom()));

        // --- 3. Afficher la femme la plus âgée ---
        System.out.println("\n--- 3. Femme la plus âgée ---");
        Femme oldest = fs.findOldestFemme();
        System.out.println("La femme la plus âgée est : " + oldest.getPrenom() + " " + oldest.getNom());

        // --- 4. Afficher les épouses d’un homme donné (entre 1990 et 2005) ---
        System.out.println("\n--- 4. Épouses de " + h1_said.getPrenom() + " " + h1_said.getNom() + " (Mariées entre 1990 et 2005) ---");
        List<Femme> epouses = hs.findEpousesBetweenDates(h1_said, date1990, date2005);
        epouses.forEach(e -> System.out.println("Épouse : " + e.getPrenom() + " " + e.getNom()));

        // --- 5. Afficher le nombre d’enfants d’une femme entre deux dates (Native Query) ---
        System.out.println("\n--- 5. Nombre d'enfants de " + f1_salima.getPrenom() + " " + f1_salima.getNom() + " (Mariages entre 1989 et 2020) ---");
        Long nbrEnfants = fs.countEnfantsBetweenDates(f1_salima.getId(), date1989, new Date(120, 0, 1));
        System.out.println("Nombre total d'enfants : " + nbrEnfants);

        // --- 6. Afficher les femmes mariées deux fois ou plus (Named Query) ---
        System.out.println("\n--- 6. Femmes mariées au moins deux fois ---");
        List<Femme> marriedTwice = fs.findMarriedAtLeastTwice();
        marriedTwice.forEach(f -> System.out.println("Femme : " + f.getPrenom() + " " + f.getNom()));

        // --- 7. Afficher les hommes mariés à quatre femmes entre deux dates (Criteria API) ---
        System.out.println("\n--- 7. Nombre d'hommes mariés à quatre femmes (Mariages entre 2000 et 2005) ---");
        Long countMen = hs.countMenMarriedToFourWomenBetweenDates(date2000, date2005);
        System.out.println("Nombre d'hommes concernés : " + countMen);

        // --- 8. Afficher les mariages d’un homme avec tous les détails ---
        System.out.println("\n--- 8. Détails des mariages de " + h1_said.getPrenom() + " " + h1_said.getNom() + " ---");
        hs.displayMariagesDetails(h1_said.getId());

        System.out.println("------------------------------------------------------------------");
    }

}
