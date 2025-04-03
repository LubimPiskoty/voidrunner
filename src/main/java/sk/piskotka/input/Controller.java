package sk.piskotka.input;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sk.piskotka.physics.Vec2;

public class Controller {
    private Map<KeyCode, InputState> keyStates;
    private Map<MouseButton, InputState> mouseStates;
    private Vec2 mousePos;


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
     * This method should be called once per frame to update input states.
     */
    public void update() {
        // Transition "PRESS" states to "HOLD" for keys
        Iterator<Map.Entry<KeyCode, InputState>> keyIterator = keyStates.entrySet().iterator();
        while (keyIterator.hasNext()) {
            Map.Entry<KeyCode, InputState> entry = keyIterator.next();
            if (entry.getValue() == InputState.PRESS) {
                entry.setValue(InputState.HOLD);
            }
        }

        // Transition "PRESS" states to "HOLD" for mouse buttons
        Iterator<Map.Entry<MouseButton, InputState>> mouseIterator = mouseStates.entrySet().iterator();
        while (mouseIterator.hasNext()) {
            Map.Entry<MouseButton, InputState> entry = mouseIterator.next();
            if (entry.getValue() == InputState.PRESS) {
                entry.setValue(InputState.HOLD);
            }
        }
    }

    public boolean isPressed(KeyCode key){
        // Logger.logInfo(getClass(), "Controller data:\nKeystates" + keyStates + "\nControllerstates:" + mouseStates);
        return keyStates.containsKey(key);
    }

    public boolean isJustPressed(KeyCode key){
        return isPressed(key) && keyStates.get(key) == InputState.PRESS;
    }

    public boolean isHolding(KeyCode key){
        return isPressed(key) && keyStates.get(key) == InputState.HOLD;
    }

    public boolean isPressed(MouseButton button){
        return mouseStates.containsKey(button);
    }

    public boolean isJustPressed(MouseButton button){
        return isPressed(button) && mouseStates.get(button) == InputState.PRESS;
    }

    public boolean isHolding(MouseButton button){
        return isPressed(button) && mouseStates.get(button) == InputState.HOLD;
    }

    public Vec2 getMousePos(){
        return mousePos;
    }

}