package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    GamePanel panel;

    public OBJ_Potion_Red(GamePanel panel) {
        super(panel);
        this.panel = panel;

        type = typeConsumable;
        name = "Red Potion";
        value = 5;
        down1 = setUp("/objects/potion_red", panel.tileSize, panel.tileSize);
        description = "[Red Potion]\nHeals " + value + " health.";
    }

    public void use(Entity entity) {
        panel.gameState = panel.dialogueState;
        panel.ui.currentDialogue = "You drink the " + name + "!\n" + "Your life has been replenished by " + value + ".";
        entity.life += value;
        panel.playSE(2);
    }

}
