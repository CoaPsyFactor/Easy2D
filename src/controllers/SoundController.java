package controllers;

import javafx.scene.media.AudioClip;

import java.util.HashMap;

/**
 *
 * Kontroler ucitavanja audio klipova
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class SoundController extends GameController {

    /**
     * Lista svih ucitanih Audio klipova
     */
    private HashMap<String, AudioClip> medias = new HashMap<>();

    /**
     * @param path Putanja do audio klipa, relativna na folder /resources/sounds
     * @return zeljeni AudioClip
     */
    public AudioClip getAudioClip(String path) {
        String resourcesPath = "/resources/sounds/";

        if (false == medias.containsKey(path)) {
            medias.put(path, new AudioClip(Class.class.getResource(resourcesPath + path).toExternalForm()));
        }

        return medias.get(path);
    }
}
