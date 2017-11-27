package net.industrial.grassland.audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.util.vector.Vector3f;

public class Source {

    private int sourceId;

    private int buffer;

    public Source(int inputBuffer) {
        sourceId = AL10.alGenSources();
        AL10.alSource3f(sourceId, AL10.AL_POSITION, 0f,0f,0f);

        buffer = inputBuffer;
    }

    public  void play() {
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    public  void playAt(Vector3f pos) {
        stop();
        setPosition(pos);
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    public void delete() {
        AL10.alDeleteSources(sourceId);
    }

    public void setVolume(float volume) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
    }

    public void setPosition(Vector3f pos) {
        AL10.alSource3f(sourceId, AL10.AL_POSITION, pos.x, pos.y, pos.z);
    }

    public void setVelocity(Vector3f vel) {
        AL10.alSource3f(sourceId, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
    }

    public void setLooping(boolean loop) {
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void pause() {
        AL10.alSourcePause(sourceId);
    }

    public void continuePlaying() {
        AL10.alSourcePlay(sourceId);
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }



}
