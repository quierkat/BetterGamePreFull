package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    public OBJ_Chest(GamePanel panel) {
        super(panel);
        name = "Chest";
        down1 = setUp("/objects/chest", panel.tileSize, panel.tileSize);
    }

}
