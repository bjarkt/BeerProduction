package org.grp2;

import org.grp2.dao.ErpDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ErpDAOTest {

    @Mock
    private ErpDAO erpDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrderAndAddOrderItems() {
        when(erpDao.getNewOrderNumber()).thenReturn(1);
        when(erpDao.addOrderItem(any(Integer.class), any(String.class), any(Integer.class))).thenReturn(true);

        int orderNumber = erpDao.getNewOrderNumber();
        boolean success = erpDao.addOrderItem(orderNumber, "pilsner", 100);

        System.out.println(success);
    }
}
