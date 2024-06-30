package com.example.wms;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product(1, "Test Product", 10);
    }

    @Test
    public void testGetters() {
        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(10, product.getQuantity());
    }

    @Test
    public void testSetters() {
        product.setName("Updated Name");
        product.setQuantity(20);

        assertEquals("Updated Name", product.getName());
        assertEquals(20, product.getQuantity());
    }

    @Test
    public void testConstructor() {
        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(10, product.getQuantity());
    }

    @Test
    public void testSetName() {
        product.setName("New Name");
        assertEquals("New Name", product.getName());
    }

    @Test
    public void testSetQuantity() {
        product.setQuantity(15);
        assertEquals(15, product.getQuantity());
    }


}
