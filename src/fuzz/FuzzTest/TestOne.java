package fuzz.FuzzTest;


import Domain.Board.Board;
import org.junit.jupiter.api.Test;



public class TestOne {

    /**
     * Runs a fuzz test on Level One by passing the Level1 game state to the test method.
     * <p>
     * This test method executes a fuzz test for the Level One configuration.
     * The {@link FuzzTest#test(Board.GameState)} method is used to initiate
     * the test with the Level1 game state.
 */
    @Test
    public void testOne () {
        FuzzTest.test(Board.GameState.LEVEL1);//Starts Test on level one
    }
}
