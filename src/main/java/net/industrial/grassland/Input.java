package net.industrial.grassland;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.industrial.grassland.graphics.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.util.glu.GLU.*;

public class Input {
    private int keys, mouseButtons;

    private boolean[] currentKeys;
    private boolean[] previousKeys;
    private boolean[] mouseEvents;

    private int previousMouseX, previousMouseY,
            currentMouseX, currentMouseY;

    public Input() {
        Keyboard.enableRepeatEvents(true);
        keys = Keyboard.getKeyCount();
        currentKeys = new boolean[keys];
        previousKeys = new boolean[keys];
     
        mouseButtons = Mouse.getButtonCount();
        mouseEvents = new boolean[mouseButtons];
    }

    public void update() {
        previousMouseX = currentMouseX;
        previousMouseY = currentMouseY;
        currentMouseX = getMouseX();
        currentMouseY = getMouseY();
     
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
        return getMouseX() - previousMouseX;
    }

    public int getMouseDY() {
        return getMouseY() - previousMouseY;
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
            (Math.min(Display.getWidth(), Display.getHeight()));
    }

    public float getMouseGLY() {
        return (float) (- getMouseY() + Display.getHeight() / 2) / 
            (Math.min(Display.getWidth(), Display.getHeight()));
    }

    public float getMaxGLX() {
        return (float) Display.getWidth() / 
            (2 * Math.min(Display.getWidth(), Display.getHeight()));
    }

    public float getMaxGLY() {
        return (float) Display.getHeight() / 
            (2 * Math.min(Display.getWidth(), Display.getHeight()));
    }

    public boolean isMouseInArea(int x, int y, int width, int height) {
        if (getMouseX() < x || getMouseX() > x + width) return false;
        if (getMouseY() < y || getMouseY() > y + height) return false;
        return true;
    }

    public Vector3f getWorldSpaceMouse() {
        Vector3f window = new Vector3f(Mouse.getX(), Mouse.getY(), 0.1f);
        
        FloatBuffer position = BufferUtils.createFloatBuffer(4);
        FloatBuffer model = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
     
        glGetFloat(GL_MODELVIEW_MATRIX, model);
        glGetFloat(GL_PROJECTION_MATRIX, projection);
        glGetInteger(GL_VIEWPORT, viewport);
        gluUnProject(window.x, window.y, window.z, 
                model, projection, viewport, position);
        position.rewind();
        float x = position.get();
        float y = position.get();
        float z = position.get();
      
        return new Vector3f(x, y, z);
    }
}
