package net.industrial.grassland;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Input {
    private int keys, mouseButtons;

    private boolean[] currentKeys;
    private boolean[] previousKeys;
    private boolean[] mouseEvents;

    public Input() {
        Keyboard.enableRepeatEvents(true);
        keys = Keyboard.getKeyCount();
        currentKeys = new boolean[keys];
        previousKeys = new boolean[keys];
     
        mouseButtons = Mouse.getButtonCount();
        mouseEvents = new boolean[mouseButtons];
    }

    public void update() {
        Keyboard.poll();
        currentKeys = new boolean[keys];
        previousKeys = new boolean[keys];
     
        while (Keyboard.next()) {
            if(Keyboard.getEventKeyState()) {
                if (Keyboard.isRepeatEvent()) 
                    previousKeys[Keyboard.getEventKey()] = true;
                else currentKeys[Keyboard.getEventKey()] = true;
            }
        }
      
        Mouse.poll();
        mouseEvents = new boolean[mouseButtons];
     
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) 
                mouseEvents[Mouse.getEventButton()] = true;
        }
    }

    public boolean isKeyPressed(int key) {
        return  currentKeys[key] && !previousKeys[key];
    }

    public boolean isKeyDown(int key) {
        return Keyboard.isKeyDown(key);
    }

    public boolean isMouseButtonPressed(int button) {
        return mouseEvents[button];
    }

    public boolean isMouseButtonDown(int button) {
        return Mouse.isButtonDown(button);
    }

    public int getMouseX() {
        return Mouse.getX();
    }

    public int getMouseY() {
        return Display.getHeight() - Mouse.getY() - 1;
    }

    public int getMouseDX() {
        return Mouse.getDX();
    }

    public int getMouseDY() {
        return - Mouse.getDY();
    }

    public int getDWheel() {
        return Mouse.getDWheel();
    }

    public void setMouseGrabbed(boolean grabbed) {
        Mouse.setGrabbed(grabbed);
    }

    public boolean isMouseGrabbed() {
        return Mouse.isGrabbed();
    }

    public float getMouseGLX() {
        return (float) (getMouseX() - Display.getWidth() / 2) / 
            (Math.min(Display.getWidth(), Display.getHeight()) / 2);
    }

    public float getMouseGLY() {
        return (float) (- getMouseY() + Display.getHeight() / 2) / 
            (Math.min(Display.getWidth(), Display.getHeight()) / 2);
    }

    public boolean isMouseInArea(int x, int y, int width, int height) {
        if (getMouseX() < x || getMouseX() > x + width) return false;
        if (getMouseY() < y || getMouseY() > y + height) return false;
        return true;
    }
}
