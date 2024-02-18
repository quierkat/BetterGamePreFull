package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

    public OBJ_Axe(GamePanel panel) {
        super(panel);

        type = typeAxe;
        name = "Woodcutter's Axe";
        down1 = setUp("/objects/axe", panel.tileSize, panel.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[Woodcutter's Axe]\nA bit rusty, but can still\ncut some trees";
    }
}
