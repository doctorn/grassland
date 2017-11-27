package net.industrial.grassland.audio;

import net.industrial.grassland.GrasslandException;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.openal.AL.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class Sound {
    private int sourceId;
    private int buffer;

    public Sound(String file) 
            throws GrasslandException {
        sourceId = alGenSources();
        alSource3f(sourceId, AL_POSITION, 0f,0f,0f);
        buffer = AudioMaster.loadSound(file);
    }

    public void play() {
        stop();
        alSourcei(sourceId, AL_BUFFER, buffer);
        continuePlaying();
    }

    public void playAt(Vector3f pos) {
        stop();
        setPosition(pos);
        alSourcei(sourceId, AL_BUFFER, buffer);
        continuePlaying();
    }

    public void delete() {
        alDeleteSources(sourceId);
    }

    public void setVolume(float volume) {
        alSourcef(sourceId, AL_GAIN, volume);
    }

    public void setPitch(float pitch) {
        alSourcef(sourceId, AL_PITCH, pitch);
    }

    public void setPosition(Vector3f pos) {
        alSource3f(sourceId, AL_POSITION, pos.x, pos.y, pos.z);
    }

    public void setVelocity(Vector3f vel) {
        alSource3f(sourceId, AL_VELOCITY, vel.x, vel.y, vel.z);
    }

    public void setLooping(boolean loop) {
        alSourcei(sourceId, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceId, AL_SOURCE_STATE) == AL_PLAYING;
    }

    public void pause() {
        alSourcePause(sourceId);
    }

    public void continuePlaying() {
        alSourcePlay(sourceId);
    }

    public void stop() {
        alSourceStop(sourceId);
    }
}
