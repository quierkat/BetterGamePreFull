package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

    public OBJ_Key(GamePanel panel) {
        super(panel);
        name = "Key";
        down1 = setUp("/objects/key", panel.tileSize, panel.tileSize);
        description = "[" + name + "]\nIt opens a door.";
    }
}
