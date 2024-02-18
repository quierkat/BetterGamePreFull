package tile_interactive;

import main.GamePanel;

public class IT_Trunk extends InteractiveTile {

    GamePanel panel;

    public IT_Trunk(GamePanel panel, int col, int row) {
        super(panel, col, row);
        this.panel = panel;

        this.worldX = panel.tileSize * col;
        this.worldY = panel.tileSize * row;

        down1 = setUp("/tiles_interactive/trunk", panel.tileSize, panel.tileSize);

        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0;
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

}
