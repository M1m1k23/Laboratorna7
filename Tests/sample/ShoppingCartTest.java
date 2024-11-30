package sample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    /**
     * Test of appendFormatted method, of class ShoppingCart.
     */
    @Test
    void testAppendFormatted() {
        StringBuilder sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 0, 14);
        assertEquals(" SomeLine ", sb.toString(), "Test with width 14");

        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 0, 15);
        assertEquals(" SomeLine ", sb.toString(), "Test with width 15");

        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 0, 5);
        assertEquals("SomeL ", sb.toString(), "Test with width 5");

        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", 1, 15);
        assertEquals(" SomeLine ", sb.toString(), "Test with width 15, align left");

        sb = new StringBuilder();
        ShoppingCart.appendFormatted(sb, "SomeLine", -1, 15);
        assertEquals("SomeLine ", sb.toString(), "Test with negative alignment");
    }

    /**
     * Test of calculateDiscount method, of class ShoppingCart.
     */
    @Test
    void testCalculateDiscount() {
        assertEquals(80, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SALE, 500), "Sale item discount for 500 units");
        assertEquals(73, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SALE, 30), "Sale item discount for 30 units");
        assertEquals(71, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SALE, 10), "Sale item discount for 10 units");
        assertEquals(70, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SALE, 9), "Sale item discount for 9 units");
        assertEquals(70, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SALE, 1), "Sale item discount for 1 unit");
        assertEquals(0, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.NEW, 20), "New item discount for 20 units");
        assertEquals(0, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.NEW, 10), "New item discount for 10 units");
        assertEquals(0, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.NEW, 1), "New item discount for 1 unit");
        assertEquals(80, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SECOND_FREE, 500), "Second Free item discount for 500 units");
        assertEquals(53, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SECOND_FREE, 30), "Second Free item discount for 30 units");
        assertEquals(51, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SECOND_FREE, 10), "Second Free item discount for 10 units");
        assertEquals(50, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SECOND_FREE, 9), "Second Free item discount for 9 units");
        assertEquals(50, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SECOND_FREE, 2), "Second Free item discount for 2 units");
        assertEquals(0, ShoppingCart.calculateDiscount(ShoppingCart.ItemType.SECOND_FREE, 1), "Second Free item discount for 1 unit");
    }

    @Test
    void testAddNewItemWithInvalidTitle() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("", 0.99, 5, ShoppingCart.ItemType.NEW);
        });
        assertEquals("Illegal title", exception.getMessage());
    }

    @Test
    void testAddNewItemWithInvalidPrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("Apple", 0.00, 5, ShoppingCart.ItemType.NEW);
        });
        assertEquals("Illegal price", exception.getMessage());
    }

    @Test
    void testAddNewItemWithInvalidQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("Apple", 0.99, 0, ShoppingCart.ItemType.NEW);
        });
        assertEquals("Illegal quantity", exception.getMessage());
    }

    @Test
    void testFormatTicketWithNoItems() {
        assertEquals("No items.", cart.formatTicket());
    }

    @Test
    void testDiscountForSecondFreeItem() {
        cart.addItem("Banana", 20.00, 4, ShoppingCart.ItemType.SECOND_FREE);
        String ticket = cart.formatTicket();
        assertTrue(ticket.contains("50%"));
        assertTrue(ticket.contains("$40.00"));
    }

    @Test
    void testDiscountForSaleItem() {
        cart.addItem("Toilet Paper", 10.00, 2, ShoppingCart.ItemType.SALE);
        String ticket = cart.formatTicket();
        assertTrue(ticket.contains("70%"));
        assertTrue(ticket.contains("$6.00"));
    }

    @Test
    void testRegularItemDiscountBasedOnQuantity() {
        cart.addItem("Nails", 2.00, 20, ShoppingCart.ItemType.REGULAR);
        String ticket = cart.formatTicket();
        assertTrue(ticket.contains("2%"));
        assertTrue(ticket.contains("$39.20"));
    }

    @Test
    void testMaxDiscountForRegularItem() {
        cart.addItem("Nails", 2.00, 1000, ShoppingCart.ItemType.REGULAR);
        String ticket = cart.formatTicket();
        assertTrue(ticket.contains("80%"));
        assertTrue(ticket.contains("$400.00"));
    }
}