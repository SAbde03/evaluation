package ma.projet.presentation;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.services.CategorieService;
import ma.projet.services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Application {

    private final CategorieService categorieService;
    private final ProduitService produitService;
    private final Scanner scanner;

    @Autowired
    public Application(CategorieService categorieService, ProduitService produitService) {
        this.categorieService = categorieService;
        this.produitService = produitService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Entrez votre choix: ");

            switch (choice) {
                case 1:
                    listCategories();
                    break;
                case 2:
                    addCategory();
                    break;
                case 3:
                    updateCategory();
                    break;
                case 4:
                    deleteCategory();
                    break;
                case 5:
                    listProducts();
                    break;
                case 6:
                    listProductsByCategory();
                    break;
                case 0:
                    running = false;
                    System.out.println("Au revoir!");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n===== Gestion de Stock =====");
        System.out.println("1. Lister les catégories");
        System.out.println("2. Ajouter une catégorie");
        System.out.println("3. Modifier une catégorie");
        System.out.println("4. Supprimer une catégorie");
        System.out.println("5. Lister les produits");
        System.out.println("6. Lister les produits par catégorie");
        System.out.println("0. Quitter");
        System.out.println("===========================");
    }

    private void listCategories() {
        List<Categorie> categories = categorieService.findAll();
        if (categories.isEmpty()) {
            System.out.println("Aucune catégorie trouvée.");
            return;
        }

        System.out.println("\n===== Liste des Catégories =====");
        for (Categorie categorie : categories) {
            System.out.println("ID: " + categorie.getId() + 
                               " | Code: " + categorie.getCode() + 
                               " | Libellé: " + categorie.getLibelle());
        }
    }

    private void addCategory() {
        System.out.println("\n===== Ajouter une Catégorie =====");
        String code = getStringInput("Entrez le code: ");
        String libelle = getStringInput("Entrez le libellé: ");

        Categorie categorie = new Categorie(code, libelle);
        boolean success = categorieService.create(categorie);

        if (success) {
            System.out.println("Catégorie ajoutée avec succès!");
        } else {
            System.out.println("Erreur lors de l'ajout de la catégorie.");
        }
    }

    private void updateCategory() {
        System.out.println("\n===== Modifier une Catégorie =====");
        int id = getIntInput("Entrez l'ID de la catégorie à modifier: ");

        Categorie categorie = categorieService.findById(id);
        if (categorie == null) {
            System.out.println("Catégorie non trouvée.");
            return;
        }

        String code = getStringInput("Entrez le nouveau code (actuel: " + categorie.getCode() + "): ");
        String libelle = getStringInput("Entrez le nouveau libellé (actuel: " + categorie.getLibelle() + "): ");

        categorie.setCode(code);
        categorie.setLibelle(libelle);

        boolean success = categorieService.update(categorie);

        if (success) {
            System.out.println("Catégorie modifiée avec succès!");
        } else {
            System.out.println("Erreur lors de la modification de la catégorie.");
        }
    }

    private void deleteCategory() {
        System.out.println("\n===== Supprimer une Catégorie =====");
        int id = getIntInput("Entrez l'ID de la catégorie à supprimer: ");

        Categorie categorie = categorieService.findById(id);
        if (categorie == null) {
            System.out.println("Catégorie non trouvée.");
            return;
        }

        boolean confirm = getBooleanInput("Êtes-vous sûr de vouloir supprimer cette catégorie? (o/n): ");
        if (!confirm) {
            System.out.println("Suppression annulée.");
            return;
        }

        boolean success = categorieService.delete(categorie);

        if (success) {
            System.out.println("Catégorie supprimée avec succès!");
        } else {
            System.out.println("Erreur lors de la suppression de la catégorie.");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("o") || input.equals("oui")) {
                return true;
            } else if (input.equals("n") || input.equals("non")) {
                return false;
            } else {
                System.out.println("Veuillez entrer 'o' pour oui ou 'n' pour non.");
            }
        }
    }

    private void listProducts() {
        List<Produit> produits = produitService.findAll();
        if (produits.isEmpty()) {
            System.out.println("Aucun produit trouvé.");
            return;
        }

        System.out.println("\n===== Liste des Produits =====");
        for (Produit produit : produits) {
            System.out.println("ID: " + produit.getId() + 
                               " | Référence: " + produit.getReference() + 
                               " | Prix: " + produit.getPrix() + 
                               " | Catégorie: " + (produit.getCategorie() != null ? produit.getCategorie().getLibelle() : "N/A"));
        }
    }

    private void listProductsByCategory() {
        System.out.println("\n===== Lister les Produits par Catégorie =====");
        int id = getIntInput("Entrez l'ID de la catégorie: ");

        Categorie categorie = categorieService.findById(id);
        if (categorie == null) {
            System.out.println("Catégorie non trouvée.");
            return;
        }

        List<Produit> produits = produitService.findByCategorie(categorie);
        if (produits.isEmpty()) {
            System.out.println("Aucun produit trouvé pour cette catégorie.");
            return;
        }

        System.out.println("\n===== Produits de la catégorie " + categorie.getLibelle() + " =====");
        for (Produit produit : produits) {
            System.out.println("ID: " + produit.getId() + 
                               " | Référence: " + produit.getReference() + 
                               " | Prix: " + produit.getPrix());
        }
    }

    // Main method moved to Main class
}
