package net.industrial.grassland.audio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import net.industrial.grassland.GrasslandException;
import net.industrial.grassland.graphics.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.util.WaveData;
import static org.lwjgl.openal.AL.*;
import static org.lwjgl.openal.AL10.*;

public class AudioMaster {
    private static List<Integer> buffers = new ArrayList<Integer>();

    public static void init() 
            throws GrasslandException {
        try {
            create();
        } catch (LWJGLException e) {
            throw new GrasslandException();
        }
    }

    public static void setListenerData(Vector3f listenerPos) {
        alListener3f(AL_POSITION,listenerPos.x,listenerPos.y,listenerPos.z);
        alListener3f(AL_VELOCITY,0f,0f,0f);
    }

    public static int loadSound(String file) 
            throws GrasslandException {
        int buffer = alGenBuffers();
        buffers.add(buffer);
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
        } catch(FileNotFoundException e) {
            throw new GrasslandException();
        }
     
        WaveData waveFile = WaveData.create(bin);
     
        alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    public static void cleanUp() {
        for(int buffer: buffers) alDeleteBuffers(buffer);
        destroy();
    }
}
