package dao;

import model.Cart;
import model.Product;
import model.Ticket;

import java.util.List;

public interface ApplicationDAO {
    void initSession();

    String login(String username, String password);

    String register(String username, String password);

    List<Product> findAllProducts();

    List<Cart> findAllCarts();

    List<Ticket> findAllTickets();

    void persistCart(List<Cart> cart);

    void persistTicket(Ticket ticket);
}
