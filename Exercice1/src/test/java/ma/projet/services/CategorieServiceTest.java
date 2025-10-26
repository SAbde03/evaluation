package ma.projet.services;

import ma.projet.classes.Categorie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CategorieServiceTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Categorie> query;

    @InjectMocks
    private CategorieService categorieService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testCreate() {
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        boolean result = categorieService.create(categorie);
        assertTrue(result);
        verify(session).save(categorie);
    }

    @Test
    public void testUpdate() {

        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);

        boolean result = categorieService.update(categorie);
        assertTrue(result);
        verify(session).update(categorie);
    }

    @Test
    public void testDelete() {
        Categorie categorie = new Categorie("CAT1", "Catégorie 1");
        categorie.setId(1);
        boolean result = categorieService.delete(categorie);
        assertTrue(result);
        verify(session).delete(categorie);
    }

    @Test
    public void testFindById() {
        int id = 1;
        Categorie expectedCategorie = new Categorie("CAT1", "Catégorie 1");
        expectedCategorie.setId(id);
        when(session.get(Categorie.class, id)).thenReturn(expectedCategorie);

        Categorie result = categorieService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("CAT1", result.getCode());
        assertEquals("Catégorie 1", result.getLibelle());
        verify(session).get(Categorie.class, id);
    }

    @Test
    public void testFindAll() {

        List<Categorie> expectedCategories = new ArrayList<>();
        expectedCategories.add(new Categorie("CAT1", "Catégorie 1"));
        expectedCategories.add(new Categorie("CAT2", "Catégorie 2"));

        when(session.createQuery("from Categorie", Categorie.class)).thenReturn(query);
        when(query.list()).thenReturn(expectedCategories);


        List<Categorie> result = categorieService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery("from Categorie", Categorie.class);
        verify(query).list();
    }
}