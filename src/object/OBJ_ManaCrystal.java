package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    GamePanel panel;

    public OBJ_ManaCrystal(GamePanel panel) {
        super(panel);
        this.panel = panel;

        type = typePickupOnly;
        value = 1;
        down1 = setUp("/objects/manacrystal_full", panel.tileSize, panel.tileSize);
        name = "Mana Crystal";
        image = setUp("/objects/manacrystal_full", panel.tileSize, panel.tileSize);
        image2 = setUp("/objects/manacrystal_blank", panel.tileSize, panel.tileSize);
    }

    public void use(Entity entity) {
        panel.playSE(2);
        panel.ui.addMessage("Mana +" + value);
        entity.mana += value;
    }

}
