package fr.esiee.baba.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import com.github.forax.zen.ApplicationContext;

import fr.esiee.baba.model.Cellule;

/**
 * Manages the graphical display of the game board in "BABA IS YOU".
 * This class handles drawing game elements and the board itself on the screen.
 */
public class View {
    private int xOrigin;
    private int yOrigin;
    private int height;
    private int width;
    private int blockSize;
    private List<List<Cellule>> grid;
    private ImageLoader imageLoader;

    /**
     * Creates a View instance to manage how the game's grid is displayed on the screen.
     *
     * @param xOrigin  the x-coordinate of the origin of the game board on the screen
     * @param yOrigin  the y-coordinate of the origin of the game board on the screen
     * @param height   the total height of the display area for the game
     * @param width    the total width of the display area for the game
     * @param blockSize the size of each block representing a single cell on the game board
     * @param grid     the grid containing all the cell elements to be displayed
     */
    public View(int xOrigin, int yOrigin, int height, int width, int blockSize, List<List<Cellule>> grid) {
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.height = height;
        this.width = width;
        this.blockSize = blockSize;
        this.grid = grid;
        this.imageLoader = new ImageLoader();
    }

    /**
     * Initializes the View instance with calculated dimensions based on the grid size and screen dimensions.
     *
     * @param grid   the game grid to be displayed
     * @param height the total height of the screen
     * @param width  the total width of the screen
     * @return the initialized View object configured for displaying the game
     */
    public static View initGameGraphics(List<List<Cellule>> grid, int height, int width) {
        Objects.requireNonNull(grid, "Grid must not be null");
        int blockSize, xOrigin, yOrigin;
        var numRows = grid.size();
        var numCols = grid.get(0).size();

        var rowRatio = height / numRows;
        var colRatio = width / numCols;
        blockSize = (int) Math.min(rowRatio, colRatio);

        xOrigin = (width - (numCols * blockSize)) / 2;
        yOrigin = (height - (numRows * blockSize)) / 2;

        return new View(xOrigin, yOrigin, height, width, blockSize, grid);
    }

    /**
     * Converts the row index into a y-coordinate on the screen based on the block size.
     *
     * @param j the row index
     * @return the y-coordinate for the row
     */
    private float yFromJ(int j) {
        return yOrigin + j * blockSize;
    }


    /**
     * Converts the column index into an x-coordinate on the screen based on the block size.
     *
     * @param i the column index
     * @return the x-coordinate for the column
     */
    private float xFromI(int i) {
        return xOrigin + i * blockSize;
    }

    /**
     * Draws an image within a specified rectangle on the graphics context provided.
     *
     * @param graphics the Graphics2D context on which the image is to be drawn
     * @param imageIcon the image icon to draw
     * @param x the x-coordinate where the image should be drawn
     * @param y the y-coordinate where the image should be drawn
     * @param dimX the width of the image
     * @param dimY the height of the image
     */
    private void drawImage(Graphics2D graphics, ImageIcon imageIcon, float x, float y, float dimX, float dimY) {
        Objects.requireNonNull(graphics, "Graphics context must not be null");
        Objects.requireNonNull(imageIcon, "ImageIcon must not be null");

        var image = imageIcon.getImage();
        var width = image.getWidth(null);
        var height = image.getHeight(null);
        var scale = Math.min(dimX / width, dimY / height);
        var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
                y + (dimY - scale * height) / 2);
        graphics.drawImage(image, transform, null);
    }

    /**
     * Draws the graphical representation of a single cell located at specified row and column indices.
     *
     * @param graphics the graphics context
     * @param i the row index of the cell
     * @param j the column index of the cell
     */
    private void drawCell(Graphics2D graphics, int i, int j) {
        Objects.requireNonNull(graphics, "Graphics context must not be null");

        var x = xFromI(j);
        var y = yFromJ(i);
        var cell = grid.get(i).get(j).getElements();
        for (var element : cell) {
            var imageIcon = imageLoader.getImageIcon(element);
            drawImage(graphics, imageIcon, x + 2, y + 2, blockSize, blockSize);
        }
    }

    /**
     * Draws the entire game board on the provided graphics context.
     * This method iterates over the entire grid, drawing each cell.
     *
     * @param graphics the graphics context
     * @param grid the grid of cells representing the game board
     * @throws NullPointerException if graphics or grid is null
     */
    private void draw(Graphics2D graphics, List<List<Cellule>> grid) {
        Objects.requireNonNull(graphics, "Graphics context must not be null");
        Objects.requireNonNull(grid, "Grid must not be null");

        graphics.setColor(Color.BLACK);
        graphics.fill(new Rectangle2D.Float(0, 0, width, height));

        for (var i = 0; i < grid.size(); i++) {
            for (var j = 0; j < grid.get(i).size(); j++) {
                drawCell(graphics, i, j);
            }
            
        }
    }

    /**
     * Renders the game board on the application's display area.
     * This is the main method used to update the graphical display of the game.
     *
     * @param context the application context used for handling the rendering
     * @param grid the grid to be displayed
     * @param view the View object managing the drawing parameters and operations
     * @throws NullPointerException if any of the parameters are null
     */
    public static void draw(ApplicationContext context, List<List<Cellule>> grid, View view) {
        Objects.requireNonNull(context, "Application context must not be null");
        Objects.requireNonNull(grid, "Grid must not be null");
        Objects.requireNonNull(view, "View must not be null");

        context.renderFrame(graphics -> view.draw(graphics, grid));
    }
}
