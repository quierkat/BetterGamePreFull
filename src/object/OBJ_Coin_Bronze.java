package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {

    GamePanel panel;

    public OBJ_Coin_Bronze(GamePanel panel) {
        super(panel);
        this.panel = panel;

        type = typePickupOnly;
        name = "Bronze Coin";
        value = 1;
        down1 = setUp("/objects/coin_bronze", panel.tileSize, panel.tileSize);
    }

    public void use(Entity entity) {
        panel.playSE(1);
        panel.ui.addMessage("Coins +" + value);
        panel.player.coins += value;
    }

}
