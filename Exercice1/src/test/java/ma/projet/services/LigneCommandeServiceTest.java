package ma.projet.services;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LigneCommandeServiceTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<LigneCommandeProduit> query;

    @InjectMocks
    private LigneCommandeService ligneCommandeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testCreate() {

        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        
        Produit produit = new Produit("REF001", 100.0f, categorie);
        produit.setId(1);
        
        Commande commande = new Commande(LocalDate.now());
        commande.setId(1);
        
        LigneCommandeProduit ligne = new LigneCommandeProduit(2, produit, commande);

        boolean result = ligneCommandeService.create(ligne);

        assertTrue(result);
        verify(session).save(ligne);
    }

    @Test
    public void testUpdate() {

        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        
        Produit produit = new Produit("REF001", 100.0f, categorie);
        produit.setId(1);
        
        Commande commande = new Commande(LocalDate.now());
        commande.setId(1);
        
        LigneCommandeProduit ligne = new LigneCommandeProduit(2, produit, commande);
        ligne.setId(1);

        boolean result = ligneCommandeService.update(ligne);

        assertTrue(result);
        verify(session).update(ligne);
    }

    @Test
    public void testDelete() {

        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        
        Produit produit = new Produit("REF001", 100.0f, categorie);
        produit.setId(1);
        
        Commande commande = new Commande(LocalDate.now());
        commande.setId(1);
        
        LigneCommandeProduit ligne = new LigneCommandeProduit(2, produit, commande);
        ligne.setId(1);

        boolean result = ligneCommandeService.delete(ligne);

        assertTrue(result);
        verify(session).delete(ligne);
    }

    @Test
    public void testFindById() {

        int id = 1;
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        
        Produit produit = new Produit("REF001", 100.0f, categorie);
        produit.setId(1);
        
        Commande commande = new Commande(LocalDate.now());
        commande.setId(1);
        
        LigneCommandeProduit expectedLigne = new LigneCommandeProduit(2, produit, commande);
        expectedLigne.setId(id);
        
        when(session.get(LigneCommandeProduit.class, id)).thenReturn(expectedLigne);

        LigneCommandeProduit result = ligneCommandeService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(2, result.getQuantite());
        assertEquals(produit, result.getProduit());
        assertEquals(commande, result.getCommande());
        verify(session).get(LigneCommandeProduit.class, id);
    }

    @Test
    public void testFindAll() {

        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        
        Produit produit1 = new Produit("REF001", 100.0f, categorie);
        produit1.setId(1);
        
        Produit produit2 = new Produit("REF002", 200.0f, categorie);
        produit2.setId(2);
        
        Commande commande = new Commande(LocalDate.now());
        commande.setId(1);
        
        List<LigneCommandeProduit> expectedLignes = new ArrayList<>();
        expectedLignes.add(new LigneCommandeProduit(2, produit1, commande));
        expectedLignes.add(new LigneCommandeProduit(1, produit2, commande));

        when(session.createQuery("from LigneCommandeProduit", LigneCommandeProduit.class)).thenReturn(query);
        when(query.list()).thenReturn(expectedLignes);

        List<LigneCommandeProduit> result = ligneCommandeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery("from LigneCommandeProduit", LigneCommandeProduit.class);
        verify(query).list();
    }
}