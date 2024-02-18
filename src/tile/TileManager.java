package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel panel;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel panel) {
        this.panel = panel;
        tile = new Tile[50];
        mapTileNum = new int[panel.maxWorldCols][panel.maxWorldRows];

        getTileImage();
        loadMap("/maps/worldV3.txt");
    }

    public void getTileImage() {
        // PLACEHOLDER
        setUp(0, "grass00", false);
        setUp(1, "grass00", false);
        setUp(2, "grass00", false);
        setUp(3, "grass00", false);
        setUp(4, "grass00", false);
        setUp(5, "grass00", false);
        setUp(6, "grass00", false);
        setUp(7, "grass00", false);
        setUp(8, "grass00", false);
        setUp(9, "grass00", false);
        //PLACEHOLDER

        // ACTUAL
        setUp(10, "grass00", false);
        setUp(11, "grass01", false);
        setUp(12, "water00", true);
        setUp(13, "water01", true);
        setUp(14, "water02", true);
        setUp(15, "water03", true);
        setUp(16, "water04", true);
        setUp(17, "water05", true);
        setUp(18, "water06", true);
        setUp(19, "water07", true);
        setUp(20, "water08", true);
        setUp(21, "water09", true);
        setUp(22, "water10", true);
        setUp(23, "water11", true);
        setUp(24, "water12", true);
        setUp(25, "water13", true);
        setUp(26, "road00", false);
        setUp(27, "road01", false);
        setUp(28, "road02", false);
        setUp(29, "road03", false);
        setUp(30, "road04", false);
        setUp(31, "road05", false);
        setUp(32, "road06", false);
        setUp(33, "road07", false);
        setUp(34, "road08", false);
        setUp(35, "road09", false);
        setUp(36, "road10", false);
        setUp(37, "road11", false);
        setUp(38, "road12", false);
        setUp(39, "earth", false);
        setUp(40, "wall", true);
        setUp(41, "tree", true);
    }

    public void setUp(int index, String imageName, boolean collision) {
        UtilityTool tool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = tool.scaleImage(tile[index].image, panel.tileSize, panel.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < panel.maxWorldCols && row < panel.maxWorldRows) {
                String line = br.readLine();

                while (col < panel.maxWorldCols) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == panel.maxWorldCols) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < panel.maxWorldCols && worldRow < panel.maxWorldRows) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * panel.tileSize;
            int worldY = worldRow * panel.tileSize;
            int screenX = worldX - panel.player.worldX + panel.player.screenX;
            int screenY = worldY - panel.player.worldY + panel.player.screenY;

            if (worldX + panel.tileSize > panel.player.worldX - panel.player.screenX &&
                worldX - panel.tileSize< panel.player.worldX + panel.player.screenX &&
                worldY + panel.tileSize> panel.player.worldY - panel.player.screenY &&
                worldY - panel.tileSize< panel.player.worldY + panel.player.screenY)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }

            worldCol++;

            if (worldCol == panel.maxWorldCols) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

}
