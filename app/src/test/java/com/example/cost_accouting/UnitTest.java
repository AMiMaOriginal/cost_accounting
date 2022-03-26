package com.example.cost_accouting;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitTest {

    private Model model = new Model();

    @Test
    public void toTwoNumbersAfterDecimalPoint(){
        assertEquals("2.69", model.toTwoNumbersAfterDecimalPoint(2.693433532));
        assertEquals("3.70", model.toTwoNumbersAfterDecimalPoint(3.7000000));
    }

    @Test
    public void getPreviousDate(){
        assertEquals("2022-2", model.getPreviousDate('-'));
    }
}
