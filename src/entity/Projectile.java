package entity;

import main.GamePanel;

public class Projectile extends Entity {

    Entity user;

    public Projectile(GamePanel panel) {
        super(panel);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;
    }

    public void update(){
        if (user == panel.player) {
            int monsterIndex = panel.checker.checkEntity(this, panel.monster);
            if (monsterIndex != 999) {
                panel.player.damageMonster(monsterIndex, attack);
                generateParticle(user.projectile, panel.monster[monsterIndex]);
                alive = false;
            }
        }
        if (user != panel.player) {
            boolean contactPlayer = panel.checker.checkPlayer(this);
            if (!panel.player.invincible && contactPlayer) {
                damagePlayer(attack);
                generateParticle(user.projectile, panel.player);
                alive = false;
            }
        }

        switch (direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }

        life--;
        if (life <= 0) {
            alive = false;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    public boolean haveResource(Entity user) {
        boolean haveResource = false;
        return haveResource;
    }
    public void subtractResource(Entity user) {
        user.mana -= useCost;
    }

}
