package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel panel) {
        super(panel);

        type = typeSword;
        name = "Normal Sword";
        down1 = setUp("/objects/sword_normal", panel.tileSize, panel.tileSize);
        attackValue = 1;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "[" + name + "]\nAn old sword.";
    }
}
