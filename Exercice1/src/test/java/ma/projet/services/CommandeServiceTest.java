package ma.projet.services;

import ma.projet.classes.Commande;
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

public class CommandeServiceTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Commande> query;

    @InjectMocks
    private CommandeService commandeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testCreate() {
        LocalDate date = LocalDate.now();
        Commande commande = new Commande(date);

        // Act
        boolean result = commandeService.create(commande);

        // Assert
        assertTrue(result);
        verify(session).save(commande);
    }

    @Test
    public void testUpdate() {
        LocalDate date = LocalDate.now();
        Commande commande = new Commande(date);
        commande.setId(1);

        boolean result = commandeService.update(commande);

        assertTrue(result);
        verify(session).update(commande);
    }

    @Test
    public void testDelete() {

        LocalDate date = LocalDate.now();
        Commande commande = new Commande(date);
        commande.setId(1);

        boolean result = commandeService.delete(commande);

        assertTrue(result);
        verify(session).delete(commande);
    }

    @Test
    public void testFindById() {

        int id = 1;
        LocalDate date = LocalDate.now();
        Commande expectedCommande = new Commande(date);
        expectedCommande.setId(id);
        when(session.get(Commande.class, id)).thenReturn(expectedCommande);

        Commande result = commandeService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(date, result.getDate());
        verify(session).get(Commande.class, id);
    }

    @Test
    public void testFindAll() {

        List<Commande> expectedCommandes = new ArrayList<>();
        expectedCommandes.add(new Commande(LocalDate.now()));
        expectedCommandes.add(new Commande(LocalDate.now().minusDays(1)));

        when(session.createQuery("from Commande", Commande.class)).thenReturn(query);
        when(query.list()).thenReturn(expectedCommandes);

        List<Commande> result = commandeService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(session).createQuery("from Commande", Commande.class);
        verify(query).list();
    }
}