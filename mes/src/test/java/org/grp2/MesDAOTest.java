package org.grp2;

import org.grp2.dao.MesDAO;
import org.grp2.database.DatabaseLogin;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;
import org.grp2.utility.AbstractDAOTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MesDAOTest extends AbstractDAOTest {
    private MesDAO mesDAO = new MesDAO(DatabaseLogin.TEST);

    @Before
    public void setUp() {
        cleanUpTables(mesDAO);
    }

    @After
    public void cleanUp() {
        cleanUpTables(mesDAO);
    }

    @Test
    public void testGetLockedOrders() {
        int orderNumber = getOrderNumber();
        addLockedOrder(orderNumber, 100, "pilsner");
        List<Order> orderItems =  mesDAO.getLockedOrders();
        System.out.println(orderItems);
    }

    private void addLockedOrder(int orderNumber, int quantity, String beerName) {
        mesDAO.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Order_items (quantity, order_number, status, beer_name) VALUES (?, ?, default, ?);");
            ps.setInt(1, quantity);
            ps.setInt(2, orderNumber);
            ps.setString(3, beerName);
            ps.execute();
        });
    }

    private int getOrderNumber() {
        AtomicReference<BigDecimal> orderID = new AtomicReference<>();

        mesDAO.executeQuery(conn -> {
            String insertOrderQuery = "INSERT INTO Orders VALUES(DEFAULT, DEFAULT, DEFAULT) RETURNING order_number";
            PreparedStatement ps = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orderID.set(rs.getBigDecimal(1));
            }
            ps.close();
        });

        return orderID.get().intValue();
    }

}
