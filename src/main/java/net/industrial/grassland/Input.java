package net.industrial.grassland;

import org.lwjgl.input.Keyboard;

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
}
