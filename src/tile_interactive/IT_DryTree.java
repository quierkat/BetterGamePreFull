package tile_interactive;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class IT_DryTree extends InteractiveTile {

    GamePanel panel;

    public IT_DryTree(GamePanel panel, int col, int row) {
        super(panel, col, row);
        this.panel = panel;

        this.worldX = panel.tileSize * col;
        this.worldY = panel.tileSize * row;

        down1 = setUp("/tiles_interactive/drytree", panel.tileSize, panel.tileSize);
        destructible = true;
        life = 3;
    }

    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        if (entity.currentWeapon.type == typeAxe) {
            isCorrectItem = true;
        }
        return isCorrectItem;
    }

    public void playSE() {
        panel.playSE(11);
    }

    public InteractiveTile getDestroyedForm() {
        InteractiveTile tile = new IT_Trunk(panel, worldX/panel.tileSize, worldY/panel.tileSize);
        return tile;
    }

    public Color getParticleColor() {Color color = new Color(65, 50, 30); return color;}
    public int getParticleSize() {int size = 6; return size;}
    public int getParticleSpeed() {int speed = 1; return speed;}
    public int getParticleMaxLife() {int maxLife = 20; return maxLife;}

}
