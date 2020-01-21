package utils;

import model.Cart;
import model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class ParseCartToInform {
    public List<ToInform> parse(List<Cart> carts, List<Ticket> ticket) {
        List<ToInform> list = new ArrayList<>();
        for (Ticket ticketInTickets : ticket) {
            List<Cart> cartToIterate = new ArrayList<>();

            for (Cart cartInCarts : carts) {
                if (cartInCarts.getTicketId() == ticketInTickets.getId()) {
                    cartToIterate.add(cartInCarts);
                }
            }
            list.add(new ToInform(
                            ticketInTickets.getUsername(),
                            ticketInTickets.getDate(),
                            String.valueOf(ticketInTickets.getId()),
                            cartToIterate
                    )
            );
        }
        return list;
    }

    public class ToInform {
        public String spacer = "###########################";
        public String user;
        public String date;
        public String id;
        public List<Cart> cart;

        public ToInform(String user, String date, String id, List<Cart> cart) {
            this.user = user;
            this.date = date;
            this.id = id;
            this.cart = cart;
        }
    }
}
