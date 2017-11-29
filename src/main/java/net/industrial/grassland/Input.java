package net.industrial.grassland;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Input {
    private boolean[] currentKeys;
    private boolean[] previousKeys;

    public Input() {
        Keyboard.enableRepeatEvents(true);
        int keys = Keyboard.getKeyCount();
        currentKeys = new boolean[keys];
        previousKeys = new boolean[keys];
    }

    public void update() {
        Keyboard.poll();
     
        int keys = Keyboard.getKeyCount();
        currentKeys = new boolean[keys];
        previousKeys = new boolean[keys];
     
        while (Keyboard.next()) {
            if(Keyboard.getEventKeyState()) {
                for (int i = 0; i < keys; i++){
                    if (Keyboard.getEventKey() == i)
                        if (Keyboard.isRepeatEvent()) previousKeys[i] = true;
                        else currentKeys[i] = true;
                }
            } else {
                for (int i = 0; i < keys; i++){
                    if (Keyboard.getEventKey() == i) currentKeys[i] = false;
                }
            }
        }
    }

    public boolean isKeyPressed(int key) {
        return  currentKeys[key] && !previousKeys[key];
    }

    public boolean isKeyDown(int key) {
        return Keyboard.isKeyDown(key);
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
