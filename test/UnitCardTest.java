import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import abilities.UnitAbility;
import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Card;
import structures.basic.UnitCard;
import structures.basic.UnitWrapper;

public class UnitCardTest {
    private UnitCard unitCard;
    private Card card; // Assume this is a valid Card object
    private UnitAbility ability; // This will be a dummy UnitAbility object

    @Before
    public void setUp() {
        card = new Card();
        ability = new UnitAbility() {
            @Override
            public void applyAbility(ActorRef out, GameState gameState, UnitWrapper unit) {
                // Dummy implementation
            }
        };
        unitCard = new UnitCard(5, "Test UnitCard", card, 10, 20, ability);
    }

    @Test
    public void testGetAttack() {
        int result = unitCard.getAttack();
        assertEquals(10, result);
    }

    @Test
    public void testGetHealth() {
        int result = unitCard.getHealth();
        assertEquals(20, result);
    }

    @Test
    public void testGetUnitAbility() {
        UnitAbility result = unitCard.getUnitAbility();
        assertNotNull(result);
        assertEquals(ability, result);
    }
}

