package se233.project2;

import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import se233.project2.model.Character;

import static org.junit.Assert.*;

public class CharacterTest {
    private se233.project2.model.Character character;

    @Before
    public void setup(){
        character = new Character(175, 700, 0, 0, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.SPACE,KeyCode.B);
    }

    @Test
    public void testMoveLeft() {
        // Initial position
        assertEquals(175, character.getX());
        assertEquals(700, character.getY());

        // Move right
        character.moveLeft();
        assertEquals(165, character.getX());
        assertEquals(700, character.getY());
    }

    @Test
    public void testMoveRight() {
        // Initial position
        assertEquals(175, character.getX());
        assertEquals(700, character.getY());

        // Move right
        character.moveRight();
        assertEquals(185, character.getX());
        assertEquals(700, character.getY());
    }

    @Test
    public void testStopMoving() {
        // Initial position
        assertEquals(175, character.getX());
        assertEquals(700, character.getY());

        // Move left
        character.moveLeft();
        assertEquals(165, character.getX());
        assertEquals(700, character.getY());

        // Stop moving
        character.stopMoving();
        assertEquals(165, character.getX());
        assertEquals(700, character.getY());
    }


}
