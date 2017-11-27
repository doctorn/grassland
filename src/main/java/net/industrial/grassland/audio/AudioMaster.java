package net.industrial.grassland.audio;

import com.sun.tools.corba.se.idl.InterfaceGen;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class AudioMaster {

    private static List<Integer> buffers = new ArrayList<Integer>();

    public static void init() {
        try {
            AL.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void setListenerData(Vector3f listenerPos) {
        AL10.alListener3f(AL10.AL_POSITION,listenerPos.x,listenerPos.y,listenerPos.z);
        AL10.alListener3f(AL10.AL_VELOCITY,0f,0f,0f);
    }

    public static int loadSound(String file) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        try
        {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
        }
        catch(FileNotFoundException e) {e.printStackTrace();}

        WaveData waveFile = WaveData.create(bin);

        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();
        return buffer;
    }

    public static void cleanUp() {
        for(int buffer: buffers) AL10.alDeleteBuffers(buffer);
        AL.destroy();
    }

}
