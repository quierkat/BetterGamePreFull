package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {

    GamePanel panel;

    public OBJ_Heart(GamePanel panel) {
        super(panel);
        this.panel = panel;

        type = typePickupOnly;
        value = 2;
        down1 = setUp("/objects/heart_full", panel.tileSize, panel.tileSize);
        name = "Heart";
        image = setUp("/objects/heart_full", panel.tileSize, panel.tileSize);
        image2 = setUp("/objects/heart_half", panel.tileSize, panel.tileSize);
        image3 = setUp("/objects/heart_blank", panel.tileSize, panel.tileSize);
    }

    public void use(Entity entity) {
        panel.playSE(2);
        panel.ui.addMessage("Life +" + value);
        entity.life += value;
    }

}
