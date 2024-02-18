package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

public class MON_GreenSlime extends Entity {

    GamePanel panel;

    public MON_GreenSlime(GamePanel panel) {
        super(panel);
        this.panel = panel;

        type = typeMonster;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;
        projectile = new OBJ_Rock(panel);

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setUp("/monster/greenslime_down_1", panel.tileSize, panel.tileSize);
        up2 = setUp("/monster/greenslime_down_2", panel.tileSize, panel.tileSize);
        down1 = setUp("/monster/greenslime_down_1", panel.tileSize, panel.tileSize);
        down2 = setUp("/monster/greenslime_down_2", panel.tileSize, panel.tileSize);
        left1 = setUp("/monster/greenslime_down_1", panel.tileSize, panel.tileSize);
        left2 = setUp("/monster/greenslime_down_2", panel.tileSize, panel.tileSize);
        right1 = setUp("/monster/greenslime_down_1", panel.tileSize, panel.tileSize);
        right2 = setUp("/monster/greenslime_down_2", panel.tileSize, panel.tileSize);
    }

    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25)
                direction = "up";
            if (i > 25 && i <= 50)
                direction = "down";
            if (i > 50 && i <= 75)
                direction = "left";
            if (i >= 75 && i <= 100)
                direction = "right";

            actionLockCounter = 0;
        }
        int i = new Random().nextInt(100)+1;
        if (i > 99 && !projectile.alive && shotAvailableCounter == 30) {
            projectile.set(worldX, worldY, direction, true, this);
            panel.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
    }

    public void damageReaction() {
        actionLockCounter = 0;
        direction = panel.player.direction;
    }

    public void checkDrop() {
        // CAST A DIE
        int i = new Random().nextInt(100) + 1;
        // SET THE MONSTER DROP
        if (i < 50) {
            dropItem(new OBJ_Coin_Bronze(panel));
        }
        if (i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(panel));
        }
        if (i >= 75 && i < 100) {
            dropItem(new OBJ_ManaCrystal(panel));
        }
    }

}
