package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel panel) {
        super(panel);
        name = "Boots";
        down1 = setUp("/objects/boots", panel.tileSize, panel.tileSize);
    }

}
