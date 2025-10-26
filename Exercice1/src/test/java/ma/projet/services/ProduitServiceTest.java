package ma.projet.services;

import ma.projet.classes.Categorie;
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

public class ProduitServiceTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Produit> produitQuery;

    @Mock
    private Query<Object[]> objectArrayQuery;

    @InjectMocks
    private ProduitService produitService;

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

        boolean result = produitService.create(produit);

        assertTrue(result);
        verify(session).save(produit);
    }

    @Test
    public void testUpdate() {

        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        Produit produit = new Produit("REF001", 100.0f, categorie);
        produit.setId(1);

        boolean result = produitService.update(produit);

        assertTrue(result);
        verify(session).update(produit);
    }

    @Test
    public void testDelete() {
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        Produit produit = new Produit("REF001", 100.0f, categorie);
        produit.setId(1);

        boolean result = produitService.delete(produit);

        assertTrue(result);
        verify(session).delete(produit);
    }

    @Test
    public void testFindById() {

        int id = 1;
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        Produit expectedProduit = new Produit("REF001", 100.0f, categorie);
        expectedProduit.setId(id);
        when(session.get(Produit.class, id)).thenReturn(expectedProduit);

        Produit result = produitService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("REF001", result.getReference());
        assertEquals(100.0, result.getPrix(), 0.001);
        assertEquals(categorie, result.getCategorie());
        verify(session).get(Produit.class, id);
    }

    @Test
    public void testFindAll() {
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        List<Produit> expectedProduits = new ArrayList<>();
        expectedProduits.add(new Produit("REF001", 100.0f, categorie));
        expectedProduits.add(new Produit("REF002", 200.0f, categorie));

        when(session.createQuery("from Produit", Produit.class)).thenReturn(produitQuery);
        when(produitQuery.list()).thenReturn(expectedProduits);

        List<Produit> result = produitService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery("from Produit", Produit.class);
        verify(produitQuery).list();
    }

    @Test
    public void testFindProduitsByCommande() {

        int commandeId = 1;
        List<Object[]> expectedResults = new ArrayList<>();
        expectedResults.add(new Object[]{"REF001", 100.0f, 2});
        expectedResults.add(new Object[]{"REF002", 200.0f, 1});

        String expectedHql = "select p.reference, p.prix, l.quantite from LigneCommandeProduit l join l.produit p where l.commande.id = :idCommande";
        when(session.createQuery(expectedHql, Object[].class)).thenReturn(objectArrayQuery);
        when(objectArrayQuery.setParameter("idCommande", commandeId)).thenReturn(objectArrayQuery);
        when(objectArrayQuery.list()).thenReturn(expectedResults);

        List<Object[]> result = produitService.findProduitsByCommande(commandeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery(expectedHql, Object[].class);
        verify(objectArrayQuery).setParameter("idCommande", commandeId);
        verify(objectArrayQuery).list();
    }

    @Test
    public void testFindByCategorie() {
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        List<Produit> expectedProduits = new ArrayList<>();
        expectedProduits.add(new Produit("REF001", 100.0f, categorie));
        expectedProduits.add(new Produit("REF002", 200.0f, categorie));

        String expectedHql = "from Produit p where p.categorie = :categorie";
        when(session.createQuery(expectedHql, Produit.class)).thenReturn(produitQuery);
        when(produitQuery.setParameter("categorie", categorie)).thenReturn(produitQuery);
        when(produitQuery.list()).thenReturn(expectedProduits);

        List<Produit> result = produitService.findByCategorie(categorie);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery(expectedHql, Produit.class);
        verify(produitQuery).setParameter("categorie", categorie);
        verify(produitQuery).list();
    }

    @Test
    public void testFindProduitsBetweenDates() {
        LocalDate date1 = LocalDate.of(2023, 1, 1);
        LocalDate date2 = LocalDate.of(2023, 12, 31);
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        List<Produit> expectedProduits = new ArrayList<>();
        expectedProduits.add(new Produit("REF001", 100.0f, categorie));
        expectedProduits.add(new Produit("REF002", 200.0f, categorie));

        String expectedHql = "select distinct l.produit from LigneCommandeProduit l where l.commande.date between :d1 and :d2";
        when(session.createQuery(expectedHql, Produit.class)).thenReturn(produitQuery);
        when(produitQuery.setParameter("d1", date1)).thenReturn(produitQuery);
        when(produitQuery.setParameter("d2", date2)).thenReturn(produitQuery);
        when(produitQuery.list()).thenReturn(expectedProduits);

        List<Produit> result = produitService.findProduitsBetweenDates(date1, date2);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery(expectedHql, Produit.class);
        verify(produitQuery).setParameter("d1", date1);
        verify(produitQuery).setParameter("d2", date2);
        verify(produitQuery).list();
    }
}