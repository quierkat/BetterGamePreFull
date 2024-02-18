package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Blue extends Entity {

    public OBJ_Shield_Blue(GamePanel panel) {
        super(panel);

        type = typeShield;
        name = "Blue Shield";
        down1 = setUp("/objects/shield_blue", panel.tileSize, panel.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nA shiny blue shield";
    }
}
