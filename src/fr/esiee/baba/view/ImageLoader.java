package fr.esiee.baba.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.ImageIcon;

import fr.esiee.baba.model.Element;

/**
 * Manages the loading and storage of image icons for game elements in "BABA IS YOU".
 * This class provides a central repository for image icons, allowing easy retrieval of visuals
 * corresponding to various game elements.
 */
public class ImageLoader {

	/**
	 *  A map that associates game elements with their corresponding image icons. 
	 */
    private Map<Element, ImageIcon> mapCharToElement;

    /**
     * Constructs an ImageLoader object and initializes the image mappings by loading the images
     * from specified file paths.
     */
    public ImageLoader() {
        mapCharToElement = new HashMap<>();
        try {
            mapCharToElement.put(Element.ENTITY_BABA, getImage("babaEntity.gif"));
            mapCharToElement.put(Element.BABA, getImage("babaWord.gif"));
            mapCharToElement.put(Element.ENTITY_WALL, getImage("brickEntity.gif"));
            mapCharToElement.put(Element.WALL, getImage("brickWord.gif"));
            mapCharToElement.put(Element.DEFEAT, getImage("defeatWord.gif"));
            mapCharToElement.put(Element.EMPTY, getImage("emptyEntity.gif"));
            mapCharToElement.put(Element.ENTITY_FLAG, getImage("flagEntity.gif"));
            mapCharToElement.put(Element.FLAG, getImage("flagWord.gif"));
            mapCharToElement.put(Element.ENTITY_FLOWER, getImage("flowerEntity.gif"));
            mapCharToElement.put(Element.FLOWER, getImage("flowerWord.gif"));
            mapCharToElement.put(Element.ENTITY_GRASS, getImage("grassEntity.gif"));
            mapCharToElement.put(Element.GRASS, getImage("grassWord.gif"));
            mapCharToElement.put(Element.HOT, getImage("hotWord.gif"));
            mapCharToElement.put(Element.IS, getImage("isWord.gif"));
            mapCharToElement.put(Element.ENTITY_LAVA, getImage("lavaEntity.gif"));
            mapCharToElement.put(Element.LAVA, getImage("lavaWord.gif"));
            mapCharToElement.put(Element.MELT, getImage("meltWord.gif"));
            mapCharToElement.put(Element.PUSH, getImage("pushWord.gif"));
            mapCharToElement.put(Element.ENTITY_ROCK, getImage("rockEntity.gif"));
            mapCharToElement.put(Element.ROCK, getImage("rockWord.gif"));
            mapCharToElement.put(Element.SINK, getImage("sinkWord.gif"));
            mapCharToElement.put(Element.ENTITY_SKULL, getImage("skullEntity.gif"));
            mapCharToElement.put(Element.SKULL, getImage("skullWord.gif"));
            mapCharToElement.put(Element.STOP, getImage("stopWord.gif"));
            mapCharToElement.put(Element.ENTITY_TILE, getImage("tileEntity.gif"));
            mapCharToElement.put(Element.TILE, getImage("tileWord.gif"));
            mapCharToElement.put(Element.ENTITY_WALL, getImage("wallEntity.gif"));
            mapCharToElement.put(Element.WALL, getImage("wallWord.gif"));
            mapCharToElement.put(Element.ENTITY_WATER, getImage("waterEntity.gif"));
            mapCharToElement.put(Element.WATER, getImage("waterWord.gif"));
            mapCharToElement.put(Element.WIN, getImage("winWord.gif"));
            mapCharToElement.put(Element.YOU, getImage("youWord.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads an image from the specified path and returns it as an ImageIcon.
     *
     * @param imagePath the path to the image file relative to the classpath
     * @return the ImageIcon loaded from the specified file
     * @throws IOException if there is an error loading the image from the specified path
     */
    private ImageIcon getImage(String imagePath) throws IOException {
        Objects.requireNonNull(imagePath, "imagePath must not be null");
        var imageUrl = ImageLoader.class.getResource("/images/" + imagePath);
        if (imageUrl == null) {
            throw new IOException("Image not found at path: /images/" + imagePath);
        }
        return new ImageIcon(imageUrl);
    }

    /**
     * Retrieves the ImageIcon associated with a given game element.
     * This method allows for the visualization of game elements using the preloaded images.
     *
     * @param element the game element whose associated ImageIcon is to be retrieved
     * @return the ImageIcon associated with the specified game element, or null if there is no image associated with the element
     */
    public ImageIcon getImageIcon(Element element) {
        return mapCharToElement.get(element);
    }
}
