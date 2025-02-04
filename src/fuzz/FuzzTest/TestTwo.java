package fuzz.FuzzTest;

import Domain.Board.Board;
import org.junit.jupiter.api.Test;

public class TestTwo {
    /**
     * Runs a fuzz test on Level One by passing the Level2 game state to the test method.
     * <p>
     * This test method executes a fuzz test for the Level Two configuration.
     * The {@link FuzzTest#test(Board.GameState)} method is used to initiate
     * the test with the Level2 game state.
     */
    @Test
    public void testTwo(){
        FuzzTest.test(Board.GameState.LEVEL2);//Starts test on level two
    }
}
