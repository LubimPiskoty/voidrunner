package sk.piskotka.input;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sk.piskotka.physics.Vec2;

/**
 * The Controller class handles input events from the keyboard and mouse.
 * It tracks the state of keys and mouse buttons and provides methods to query their states.
 */
public class Controller {
    private Map<KeyCode, InputState> keyStates;
    private Map<MouseButton, InputState> mouseStates;
    private Vec2 mousePos;

    /**
     * Constructs a Controller instance and sets up input event handlers for the given scene.
     *
     * @param scene the JavaFX Scene to attach input event handlers to
     */
    public Controller(Scene scene){
        keyStates = new HashMap<>();
        mouseStates = new HashMap<>();
        mousePos = Vec2.ZERO();

        // Assign callback
        // Key press event
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            keyStates.putIfAbsent(code, InputState.PRESS);
        });

        // Key release event
        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            keyStates.remove(code);
        });

        // Mouse press event
        scene.setOnMousePressed(event -> {
            MouseButton button = event.getButton();
            mouseStates.putIfAbsent(button, InputState.PRESS);
        });

        // Mouse release event
        scene.setOnMouseReleased(event -> {
            MouseButton button = event.getButton();
            mouseStates.remove(button);
        });

        // Mouse position tracking
        EventHandler<MouseEvent> getMousePos = event -> {
            mousePos = new Vec2(event.getSceneX(), event.getSceneY());
        };

        scene.setOnMouseMoved(getMousePos);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, getMousePos);
    }

    /**
     * Updates the input states. This method should be called once per frame.
     * Transitions "PRESS" states to "HOLD" for both keys and mouse buttons.
     */
    public void update() {
        for (Map.Entry<KeyCode, InputState> entry : keyStates.entrySet()) {
            if (entry.getValue() == InputState.PRESS) {
                entry.setValue(InputState.HOLD);
            }
        }
        // Transition "PRESS" states to "HOLD" for mouse buttons
        for (Map.Entry<MouseButton, InputState> entry : mouseStates.entrySet()) {
            if (entry.getValue() == InputState.PRESS) {
                entry.setValue(InputState.HOLD);
            }
        }
    }

    /**
     * Checks if the specified key is currently pressed.
     *
     * @param key the KeyCode to check
     * @return true if the key is pressed, false otherwise
     */
    public boolean isPressed(KeyCode key){
        // Logger.logInfo(getClass(), "Controller data:\nKeystates" + keyStates + "\nControllerstates:" + mouseStates);
        return keyStates.containsKey(key);
    }

    /**
     * Checks if the specified key was just pressed (i.e., in the "PRESS" state).
     *
     * @param key the KeyCode to check
     * @return true if the key was just pressed, false otherwise
     */
    public boolean isJustPressed(KeyCode key){
        return isPressed(key) && keyStates.get(key) == InputState.PRESS;
    }

    /**
     * Checks if the specified key is being held down (i.e., in the "HOLD" state).
     *
     * @param key the KeyCode to check
     * @return true if the key is being held, false otherwise
     */
    public boolean isHolding(KeyCode key){
        return isPressed(key) && keyStates.get(key) == InputState.HOLD;
    }

    /**
     * Checks if the specified mouse button is currently pressed.
     *
     * @param button the MouseButton to check
     * @return true if the mouse button is pressed, false otherwise
     */
    public boolean isPressed(MouseButton button){
        return mouseStates.containsKey(button);
    }

    /**
     * Checks if the specified mouse button was just pressed (i.e., in the "PRESS" state).
     *
     * @param button the MouseButton to check
     * @return true if the mouse button was just pressed, false otherwise
     */
    public boolean isJustPressed(MouseButton button){
        return isPressed(button) && mouseStates.get(button) == InputState.PRESS;
    }

    /**
     * Checks if the specified mouse button is being held down (i.e., in the "HOLD" state).
     *
     * @param button the MouseButton to check
     * @return true if the mouse button is being held, false otherwise
     */
    public boolean isHolding(MouseButton button){
        return isPressed(button) && mouseStates.get(button) == InputState.HOLD;
    }

    /**
     * Gets the current position of the mouse in the scene.
     *
     * @return a Vec2 object representing the mouse position
     */
    public Vec2 getMousePos(){
        return mousePos;
    }

}