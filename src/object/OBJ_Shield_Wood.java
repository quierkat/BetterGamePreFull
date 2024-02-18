package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {

    public OBJ_Shield_Wood(GamePanel panel) {
        super(panel);

        type = typeShield;
        name = "Wood Shield";
        down1 = setUp("/objects/shield_wood", panel.tileSize, panel.tileSize);
        defenseValue = 1;
        description = "[" + name + "]\nMade of wood.";
    }
}
