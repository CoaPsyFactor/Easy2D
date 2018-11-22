package lib.sprite;

import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.util.HashMap;

/**
 * Klasa se koristi za generisanje pojedinacnih "Sprite"-ova od skupa manjih slika na jednoj velikoj slici (spritesheet)
 * <p>
 * Sprite moze biti jednom generisan i uvek vracena ta instanca objekta (singleton)
 * Po zelji moze da se vrati uvek nova instanca "Sprite"-a
 * <p>
 * TODO: Implementirati offset, da zapravo moze generisati "Sprite" na slici koja ima razmake izmedju svakog dela
 *
 * @author Aleksandar Zivanovic <coapsyfactor@gmail.com>
 */
public class SpriteSheet implements MediaDisposer.Disposable {

    /**
     * Podrazumevana putanja direktorijuma u kojima se nalaze "spritesheet"-ovi
     */
    private static final String resourcesDirectory = "/resources/spritesheets/";

    /**
     * Singleton instance svih ucitanih "spritesheet"-ova
     */
    private static final HashMap<String, SpriteSheet> spriteSheets = new HashMap<>();

    /**
     * Slika koja predstavlja spritesheet
     */
    private Image _sheetImage = null;

    /**
     * Singleton instance svih do trenutka ucitanih "Sprite"-ova
     */
    private final HashMap<String, Sprite> _sprites = new HashMap<>();

    /**
     * Odstojanje izmedju dva "Sprite"-a na sheet-u po X osi
     */
    private int _offsetX = 0;

    /**
     * Odstojanje izmedju dva "Sprite"-a na sheet-u po Y osi
     */
    private int _offsetY = 0;

    /**
     * Sirina jednog "Sprite"-a na sheet-u
     */
    private double _tileWidth;

    /**
     * Visina jednog "Sprite"-a na sheet-u
     */
    private double _tileHeight;

    /**
     * Broj redova koje spritesheet ima (kalkulise se po visini slike i "Sprite"-a
     */
    private int _rows = 0;

    /**
     * Broj kolona koje spritesheet ima (kalkulise se po sirini slike i "Sprite"-a
     */
    private int _cols = 0;

    /**
     * @param imagePath  Putanja do spritesheet, relativna na <code>/resources/spritesheets</code> direktorijum
     * @param tileWidth  Sirina jednog "Sprite"-a
     * @param tileHeight Visina jednog "Sprite"-a
     */
    public SpriteSheet(String imagePath, double tileWidth, double tileHeight) {
        construct(imagePath, tileWidth, tileHeight);
    }

    /**
     * @param imagePath Putanja do spritesheet, relativna na <code>/resources/spritesheets</code> direktorijum
     * @param colTiles  Broj kolona u sheet-u
     * @param rowTiles  Broj redova u sheet-u
     * @param offsetX   Odstojanje izmedju "Sprite"-ova po X osi
     * @param offsetY   Odstojanje izmedju "Sprite"-ova po Y osi
     */
    public SpriteSheet(String imagePath, int colTiles, int rowTiles, int offsetX, int offsetY) {
        _sheetImage = getNewImage(imagePath);

        _rows = rowTiles;

        _cols = colTiles;

        _tileWidth = (_sheetImage.getWidth() / colTiles) - offsetX;

        _tileHeight = (_sheetImage.getHeight() / rowTiles) - offsetY;

        _offsetX = offsetX;

        _offsetY = offsetY;
    }

    /**
     * @param imagePath  Putanja do spritesheet, relativna na <code>/resources/spritesheets</code> direktorijum
     * @param tileWidth  Sirina jednog "Sprite"-a
     * @param tileHeight Visina jednog "Sprite"-a
     * @param offsetX    Odstojanje izmedju "Sprite"-ova po X osi
     * @param offsetY    Odstojanje izmedju "Sprite"-ova po Y osi
     */
    public SpriteSheet(String imagePath, double tileWidth, double tileHeight, int offsetX, int offsetY) {
        _offsetX = offsetX;

        _offsetY = offsetY;

        construct(imagePath, tileWidth - offsetX, tileHeight - offsetY);
    }

    /**
     * Vraca "singleton" instancu zeljenog sheet-a, ukoliko ne postoji instanca pravi se nova i stavlja u listu
     *
     * @param imagePath  Putanja do spritesheet, relativna na <code>/resources/spritesheets</code> direktorijum
     * @param tileWidth  Sirina jednog "Sprite"-a
     * @param tileHeight Visina jednog "Sprite"-a
     * @return Instancu trazenog "SpriteSheet"-a
     */
    public static SpriteSheet getSpriteSheet(String imagePath, double tileWidth, double tileHeight) {
        if (spriteSheets.containsKey(imagePath)) {
            return spriteSheets.get(imagePath);
        }

        spriteSheets.put(imagePath, new SpriteSheet(resourcesDirectory + imagePath, tileWidth, tileHeight));

        return spriteSheets.get(imagePath);
    }

    /**
     * Vraca novu instancu zeljenog sheet-a
     *
     * @param imagePath  Putanja do spritesheet, relativna na <code>/resources/spritesheets</code> direktorijum
     * @param tileWidth  Sirina jednog "Sprite"-a
     * @param tileHeight Visina jednog "Sprite"-a
     * @return Instancu trazenog "SpriteSheet"-a
     */
    public static SpriteSheet getNewSpriteSheet(String imagePath, double tileWidth, double tileHeight) {
        return new SpriteSheet(resourcesDirectory + imagePath, tileWidth, tileHeight);
    }

    /**
     * Ucitava novu sliku iz zadate putanje("Image"), izracunava broj redova i kolona pomocu sirine i visine jednog "Sprite"-a
     *
     * @param imagePath  Putanja do spritesheet, relativna na <code>/resources/spritesheets</code> direktorijum
     * @param tileWidth  Sirina jednog "Sprite"-a
     * @param tileHeight Visina jednog "Sprite"-a
     */
    private void construct(String imagePath, double tileWidth, double tileHeight) {
        _sheetImage = getNewImage(imagePath);

        if (0 != tileWidth) {
            _cols = (int) Math.ceil(_sheetImage.getWidth() / tileWidth);
        }

        if (0 != tileHeight) {
            _rows = (int) Math.ceil(_sheetImage.getHeight() / tileHeight);
        }

        _tileWidth = tileWidth;

        _tileHeight = tileHeight;
    }

    /**
     * Pravi novu instancu slike ("Image") iz zadate putanje
     *
     * @param imagePath Putanja do slike relativna na direktorijum <code>/resources/spritesheets</code>
     * @return Sliku ucitanu iz zadate putanje
     */
    private Image getNewImage(String imagePath) {
        try {
            return new Image(getClass().getResourceAsStream(imagePath));
        } catch (NullPointerException exception) {
            throw new RuntimeException("Path " + imagePath + " is not valid.");
        }
    }

    /**
     * Vraca novu instancu jednog "Sprite"-a ciju poziciju na sheet-u odredjuje pretvaranjem <b>id</b> parametra u
     * kolonu i red
     *
     * @param id Odstojanje zeljenog "Sprite"-a u "SpriteSheet"-u
     * @return Zeljeni "Sprite"
     */
    public Sprite getNewSprite(int id) {
        int row = (int) Math.ceil((double) id / _cols);

        int col = id - (_cols * (row - 1));

        return getNewSprite(row, col);
    }

    /**
     * Vraca novu instancu jednog "Sprite"-a ciju poziciju na sheet-u odredjuje pretvaranjem <b>id</b> parametra u
     * kolonu i red
     *
     * @param row Red u kome se nalazi zeljena slika ("Sprite")
     * @param col Kolona u kojoj se nalazi zeljena slika ("Sprite")
     * @return Zeljenu sliku ("Sprite")
     */
    public Sprite getNewSprite(int row, int col) {
        int x = (col - 1) * (int) _tileWidth;

        int y = (row - 1) * (int) _tileHeight;

        return new Sprite(new WritableImage(_sheetImage.getPixelReader(), x, y, (int) _tileWidth, (int) _tileHeight));
    }

    /**
     * Vraca vec kreiranu (singleton) instancu zeljenog "Sprite"-a i podesava mu odstojanja
     *
     * @param id      Odstojanje zeljenog "Sprite"-a u "SpriteSheet"-u
     * @param offsetX Odstojanje od pocetka (gore levo) po X osi
     * @param offsetY Odstojanje od pocetka (gore levo) po Y osi
     * @return Zeljeni "Sprite" sa podesenim odstupanjima
     */
    public Sprite getSprite(int id, double offsetX, double offsetY) {
        Sprite sprite = getSprite(id);

        sprite.setOffset(offsetX, offsetY);

        return sprite;
    }

    /**
     * Vraca vec kreiranu (singleton) instancu zeljenog "Sprite"-a
     *
     * @param id Odstojanje zeljenog "Sprite"-a u "SpriteSheet"-u
     * @return Zeljeni "Sprite"
     */
    public Sprite getSprite(int id) {
        int row = (int) Math.ceil((double) id / _cols);

        int col = id - (_cols * (row - 1));

        String key = row + "-" + col;

        if (false == _sprites.containsKey(key)) {
            _sprites.put(key, getNewSprite(row, col));
        }

        return _sprites.get(key);
    }

    @Override
    public void dispose() {
        _sheetImage = null;

        _sprites.forEach((s, sprite) -> {
            sprite.dispose();
        });

        _sprites.clear();
    }
}
