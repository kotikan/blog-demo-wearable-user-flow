package com.kotikan.demo.taxitracker.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomGeneratorTest {

    @Test
    public void hasLength5() throws Exception {
        final RandomGenerator g = new RandomGenerator();
        final String s = g.newPrice();

        assertEquals(5, s.length());
    }

    @Test
    public void contains_a_decimal_place() throws Exception {
        final RandomGenerator g = new RandomGenerator();
        final String s = g.newPrice();

        assertTrue(s.contains("."));
    }

    @Test
    public void contains_a_pound_symbol() throws Exception {
        final RandomGenerator g = new RandomGenerator();
        final String s = g.newPrice();

        assertTrue(s.contains("£"));
    }

    @Test
    public void values_are_different() throws Exception {
        final RandomGenerator g = new RandomGenerator();
        final String price1 = g.newPrice();
        final String price2 = g.newPrice();

        assertNotEquals(price1, price2);
    }

}