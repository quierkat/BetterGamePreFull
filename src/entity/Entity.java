package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel panel;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2,
            attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    String[] dialogues = new String[20];

    // STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;

    // COUNTER
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;

    // CHARACTER ATTRIBUTES
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coins;
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    // ITEM ATTRIBUTES
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;

    // TYPE
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final int typePlayer = 0;
    public final int typeNPC = 1;
    public final int typeMonster = 2;
    public final int typeSword = 3;
    public final int typeAxe = 4;
    public final int typeShield = 5;
    public final int typeConsumable = 6;
    public final int typePickupOnly = 7;

    public Entity(GamePanel panel) {
        this.panel = panel;
    }

    public void setAction() {}
    public void damageReaction() {}
    public void speak() {
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        panel.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (panel.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }

    public void use(Entity entity) {}

    public void checkDrop() {}

    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < panel.obj.length; i++) {
            if (panel.obj[i] == null) {
                panel.obj[i] = droppedItem;
                panel.obj[i].worldX = worldX; // the dead monster's worldX and worldY
                panel.obj[i].worldY = worldY;
                break;
            }
        }
    }

    public Color getParticleColor() {Color color = null; return color;}
    public int getParticleSize() {int size = 0; return size;}
    public int getParticleSpeed() {int speed = 0; return speed;}
    public int getParticleMaxLife() {int maxLife = 0; return maxLife;}

    public void generateParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(panel, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(panel, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(panel, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(panel, target, color, size, speed, maxLife, 2, 1);
        panel.particleList.add(p1);
        panel.particleList.add(p2);
        panel.particleList.add(p3);
        panel.particleList.add(p4);
    }

    public void update() {
        setAction();
        collisionOn = false;
        panel.checker.checkTile(this);
        panel.checker.checkObject(this, false);
        panel.checker.checkEntity(this, panel.npc);
        panel.checker.checkEntity(this, panel.monster);
        panel.checker.checkEntity(this, panel.iTile);
        boolean contactPlayer = panel.checker.checkPlayer(this);

        if (this.type == typeMonster && contactPlayer) {
            damagePlayer(attack);
        }

        // IF COLLISION IS FALSE, ENTITY CAN MOVE
        if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
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

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
    }

    public void damagePlayer(int attack) {
        if (!panel.player.invincible) {
            panel.playSE(6);
            int damage = attack - panel.player.defense;
            if (damage < 0) {
                damage = 0;
            }
            panel.player.life -= damage;

            panel.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - panel.player.worldX + panel.player.screenX;
        int screenY = worldY - panel.player.worldY + panel.player.screenY;

        if (worldX + panel.tileSize > panel.player.worldX - panel.player.screenX &&
                worldX - panel.tileSize< panel.player.worldX + panel.player.screenX &&
                worldY + panel.tileSize> panel.player.worldY - panel.player.screenY &&
                worldY - panel.tileSize< panel.player.worldY + panel.player.screenY)
        {
            switch (direction) {
                case "up":
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = up2;}
                    break;
                case "down":
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
                    break;
                case "left":
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
                    break;
                case "right":
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
                    break;
            }

            // Monster HP bar
            if (type == 2 && hpBarOn) {
                double oneScale = (double)panel.tileSize/maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX-1, screenY-16, panel.tileSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if (dying) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);
            changeAlpha(g2, 1f);
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        int i = 5;

        if (dyingCounter <= i) {changeAlpha(g2, 0f);}
        if (dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2, 0f);}
        if (dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2, 0f);}
        if (dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2, 0f);}
        if (dyingCounter > i*7 && dyingCounter <= i*8) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*8) {
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setUp(String imagePath, int width, int height) {
        UtilityTool tool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = tool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
