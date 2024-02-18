package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Fireball extends Projectile {

    GamePanel panel;

    public OBJ_Fireball(GamePanel panel) {
        super(panel);
        this.panel = panel;

        name = "Fireball";
        speed = 5;
        maxLife = 80;
        life = maxLife;
        attack = 2;
        useCost = 1;
        alive = false;
        getImage();
    }

    public void getImage() {
        up1 = setUp("/projectile/fireball_up_1", panel.tileSize, panel.tileSize);
        up2 = setUp("/projectile/fireball_up_2", panel.tileSize, panel.tileSize);
        down1 = setUp("/projectile/fireball_down_1", panel.tileSize, panel.tileSize);
        down2 = setUp("/projectile/fireball_down_2", panel.tileSize, panel.tileSize);
        left1 = setUp("/projectile/fireball_left_1", panel.tileSize, panel.tileSize);
        left2 = setUp("/projectile/fireball_left_2", panel.tileSize, panel.tileSize);
        right1 = setUp("/projectile/fireball_right_1", panel.tileSize, panel.tileSize);
        right2 = setUp("/projectile/fireball_right_2", panel.tileSize, panel.tileSize);
    }

    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        if (user.mana >= useCost) {
            haveResource = true;
        }
        return haveResource;
    }

    public void subtractResource(Entity user) {
        user.mana -= useCost;
    }
    public Color getParticleColor() {Color color = new Color(240, 50, 0); return color;}
    public int getParticleSize() {int size = 10; return size;}
    public int getParticleSpeed() {int speed = 1; return speed;}
    public int getParticleMaxLife() {int maxLife = 20; return maxLife;}
}
