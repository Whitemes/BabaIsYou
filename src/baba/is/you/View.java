package baba.is.you;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import com.github.forax.zen.ApplicationContext;

/**
 * The View class handles the graphical representation of the game board.
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
     * Constructor for View class.
     *
     * @param xOrigin  the x-coordinate of the origin of the board
     * @param yOrigin  the y-coordinate of the origin of the board
     * @param height   the height of the screen
     * @param width    the width of the screen
     * @param blockSize the size of each block on the board
     * @param grid     the grid representing the game board
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
     * Initializes the graphics for the game.
     *
     * @param grid   the grid representing the game board
     * @param height the height of the screen
     * @param width  the width of the screen
     * @return the initialized View instance
     * @throws NullPointerException if the grid is null
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
     * Calculates the y-coordinate from the row index.
     *
     * @param j the row index
     * @return the y-coordinate
     */
    private float yFromJ(int j) {
        return yOrigin + j * blockSize;
    }

    /**
     * Calculates the x-coordinate from the column index.
     *
     * @param i the column index
     * @return the x-coordinate
     */
    private float xFromI(int i) {
        return xOrigin + i * blockSize;
    }

    /**
     * Draws an image on the board.
     *
     * @param graphics the graphics context
     * @param imageIcon the image icon to be drawn
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param dimX the width dimension
     * @param dimY the height dimension
     * @throws NullPointerException if graphics or imageIcon is null
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
     * Draws a single cell on the board.
     *
     * @param graphics the graphics context
     * @param i the row index
     * @param j the column index
     * @throws NullPointerException if graphics is null
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
     * Draws the entire game board.
     *
     * @param graphics the graphics context
     * @param grid the grid representing the game board
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
     * Renders the game board.
     *
     * @param context the application context
     * @param grid the grid representing the game board
     * @param view the View instance
     * @throws NullPointerException if context, grid, or view is null
     */
    public static void draw(ApplicationContext context, List<List<Cellule>> grid, View view) {
        Objects.requireNonNull(context, "Application context must not be null");
        Objects.requireNonNull(grid, "Grid must not be null");
        Objects.requireNonNull(view, "View must not be null");

        context.renderFrame(graphics -> view.draw(graphics, grid));
    }
}
