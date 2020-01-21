package dao;

import model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class ApplicationDAOImpl implements ApplicationDAO {
    public Session session;

    @Override
    public void initSession() {
        //Create session factory
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        SessionFactory sessionFactory = new MetadataSources(standardServiceRegistry)
                .buildMetadata()
                .buildSessionFactory();

        //Open session
        session = sessionFactory.openSession();
    }

    @Override
    public String login(String username, String password) {
        Transaction transaction = session.beginTransaction();
        //Create objects
        Login login = new Login(username, new PasswordHasher().hash(password));
        //Make persistence
        Login login1 = (Login) session.get(Login.class, login.getUsername());
        transaction.commit();
        if (login1.getUsername() != null) {
            return "true";
        }
        return "Invalid Credentials";
    }

    @Override
    public String register(String username, String password) {
        Transaction transaction = session.beginTransaction();
        //Create objects
        Register register = new Register(username, new PasswordHasher().hash(password));
        //Make persistence
        session.save(register);

        transaction.commit();
        return "null";
    }

    @Override
    public List<Product> findAllProducts() {
        if (session == null) {
            initSession();
        }
        Transaction transaction = session.beginTransaction();
        //Create objects
        List<Product> allProducts = session.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        transaction.commit();
        return allProducts;
    }

    @Override
    public List<Cart> findAllCarts() {

        if (session == null) {
            initSession();
        }
        Transaction transaction = session.beginTransaction();
        //Create objects
        List<Cart> allCarts = session.createQuery("SELECT c FROM Cart c", Cart.class).getResultList();
        transaction.commit();
        return allCarts;
    }

    @Override
    public List<Ticket> findAllTickets() {
        if (session == null) {
            initSession();
        }
        Transaction transaction = session.beginTransaction();
        //Create objects
        List<Ticket> allTickets = session.createQuery("SELECT t FROM Ticket t", Ticket.class).getResultList();
        transaction.commit();
        return allTickets;
    }

    @Override
    public void persistCart(List<Cart> cart) {
        Transaction transaction = session.beginTransaction();
        //Make persistence
        cart.forEach(c -> session.save(c));

        transaction.commit();
    }

    @Override
    public void persistTicket(Ticket ticket) {
        Transaction transaction = session.beginTransaction();
        //Make persistence
        session.save(ticket);

        transaction.commit();
    }
}
