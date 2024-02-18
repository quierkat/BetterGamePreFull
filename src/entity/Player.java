package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {

    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    public int standCounter;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;

    public Player(GamePanel panel, KeyHandler keyHandler) {
        super(panel);
        this.keyHandler = keyHandler;

        screenX = panel.screenWidth/2 - (panel.tileSize/2);
        screenY = panel.screenHeight/2 - (panel.tileSize/2);
        // SOLID AREA
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 25;
        solidArea.height = 25;
        // ATTACK AREA
//        attackArea.width = 36;
//        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = panel.tileSize * 23;
        worldY = panel.tileSize * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10; // just for testing
        strength = 1; // More strength = more damage dealt
        dexterity = 1; // More dexterity = less damage received
        exp = 0;
        nextLevelExp = 5;
        coins = 0;
        currentWeapon = new OBJ_Sword_Normal(panel);
        currentShield = new OBJ_Shield_Wood(panel);
        projectile = new OBJ_Fireball(panel);
//        projectile = new OBJ_Rock(panel);
        attack = getAttack(); // total attack value is decided by strength and weapon
        defense = getDefense(); // total defense value is decided by strength and shield
    }

    public void setItems() {
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(panel));
    }

    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    public void getPlayerImage() {
        up1 = setUp("/player/boy_up_1", panel.tileSize, panel.tileSize);
        up2 = setUp("/player/boy_up_2", panel.tileSize, panel.tileSize);
        down1 = setUp("/player/boy_down_1", panel.tileSize, panel.tileSize);
        down2 = setUp("/player/boy_down_2", panel.tileSize, panel.tileSize);
        left1 = setUp("/player/boy_left_1", panel.tileSize, panel.tileSize);
        left2 = setUp("/player/boy_left_2", panel.tileSize, panel.tileSize);
        right1 = setUp("/player/boy_right_1", panel.tileSize, panel.tileSize);
        right2 = setUp("/player/boy_right_2", panel.tileSize, panel.tileSize);
    }

    public void getPlayerAttackImage() {
        if (currentWeapon.type == typeSword) {
            attackUp1 = setUp("/player/boy_attack_up_1", panel.tileSize, panel.tileSize * 2);
            attackUp2 = setUp("/player/boy_attack_up_2", panel.tileSize, panel.tileSize * 2);
            attackDown1 = setUp("/player/boy_attack_down_1", panel.tileSize, panel.tileSize * 2);
            attackDown2 = setUp("/player/boy_attack_down_2", panel.tileSize, panel.tileSize * 2);
            attackLeft1 = setUp("/player/boy_attack_left_1", panel.tileSize * 2, panel.tileSize);
            attackLeft2 = setUp("/player/boy_attack_left_2", panel.tileSize * 2, panel.tileSize);
            attackRight1 = setUp("/player/boy_attack_right_1", panel.tileSize * 2, panel.tileSize);
            attackRight2 = setUp("/player/boy_attack_right_2", panel.tileSize * 2, panel.tileSize);
        }
        if (currentWeapon.type == typeAxe) {
            attackUp1 = setUp("/player/boy_axe_up_1", panel.tileSize, panel.tileSize * 2);
            attackUp2 = setUp("/player/boy_axe_up_2", panel.tileSize, panel.tileSize * 2);
            attackDown1 = setUp("/player/boy_axe_down_1", panel.tileSize, panel.tileSize * 2);
            attackDown2 = setUp("/player/boy_axe_down_2", panel.tileSize, panel.tileSize * 2);
            attackLeft1 = setUp("/player/boy_axe_left_1", panel.tileSize * 2, panel.tileSize);
            attackLeft2 = setUp("/player/boy_axe_left_2", panel.tileSize * 2, panel.tileSize);
            attackRight1 = setUp("/player/boy_axe_right_1", panel.tileSize * 2, panel.tileSize);
            attackRight2 = setUp("/player/boy_axe_right_2", panel.tileSize * 2, panel.tileSize);
        }
    }

    public void update() {

        if (attacking) {
            attacking();
        }
        else if (keyHandler.upPressed || keyHandler.downPressed ||
                keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed) {
            if (keyHandler.upPressed) {
                direction = "up";
            }
            if (keyHandler.downPressed){
                direction = "down";
            }
            if (keyHandler.leftPressed){
                direction = "left";
            }
            if (keyHandler.rightPressed){
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            panel.checker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = panel.checker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION
            int npcIndex = panel.checker.checkEntity(this, panel.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = panel.checker.checkEntity(this, panel.monster);
            contactMonster(monsterIndex);

            // CHECK INTERACTIVE TILE COLLISION
            panel.checker.checkEntity(this, panel.iTile);

            // CHECK EVENT
            panel.eHandler.checkEvent();

            // IF COLLISION IS FALSE, ENTITY CAN MOVE
            if (!collisionOn && !keyHandler.enterPressed) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            if (keyHandler.enterPressed && !attackCanceled) {
                panel.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            panel.keyHandler.enterPressed = false;

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            standCounter++;
            if (standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }

        if (panel.keyHandler.shotKeyPressed && !projectile.alive &&
                shotAvailableCounter == 30 && projectile.haveResource(this)) {
            // SET DEFAULT COORDINATES, DIRECTION, AND USER
            projectile.set(worldX, worldY, direction, true, this);

            // SUBTRACT THE COST (MANA, AMMO, ETC.)
            projectile.subtractResource(this);

            // ADD IT TO THE LIST
            panel.projectileList.add(projectile);

            shotAvailableCounter = 0;
            panel.playSE(10);
        }

        // Needs to be outside key IF statement
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if (life > maxLife) {
            life = maxLife;
        }
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust the player's worldX/Y for the attackArea
            switch (direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // Check monster collision with the updated worldX, worldY, and solidArea
            int monsterIndex = panel.checker.checkEntity(this, panel.monster);
            damageMonster(monsterIndex, attack);

            int iTileIndex = panel.checker.checkEntity(this, panel.iTile);
            damageInteractiveTile(iTileIndex);

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            // PICK UP ONLY ITEMS
            if (panel.obj[i].type == typePickupOnly) {
                panel.obj[i].use(this);
                panel.obj[i] = null;
            }
            // INVENTORY ITEMS
            else {
                String text = "Picked up a " + panel.obj[i].name + "!";
                if (inventory.size() != maxInventorySize) {
                    inventory.add(panel.obj[i]);
                    panel.playSE(1);
                } else {
                    text = "You can't carry any more!";
                }
                panel.ui.addMessage(text);
                panel.obj[i] = null;
            }
        }
    }

    public void interactNPC(int i) {
        if (panel.keyHandler.enterPressed) {
            if (i != 999) {
                attackCanceled = true;
                panel.gameState = panel.dialogueState;
                panel.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible && !panel.monster[i].dying) {
                panel.playSE(6);

                int damage = panel.monster[i].attack - defense;
                if (damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i, int attack) {
        if (i != 999) {
            if (!panel.monster[i].invincible) {
                panel.playSE(5);

                int damage = attack - panel.monster[i].defense;
                if (damage < 0) {
                    damage = 0;
                }
                panel.monster[i].life -= damage;
                panel.ui.addMessage(damage + " damage!");

                panel.monster[i].invincible = true;
                panel.monster[i].damageReaction();

                if (panel.monster[i].life <= 0) {
                    panel.monster[i].dying = true;
                    panel.ui.addMessage("Killed the " + panel.monster[i].name +"!");
                    panel.ui.addMessage("Exp + " + panel.monster[i].exp);
                    exp += panel.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void damageInteractiveTile(int i) {
        if (i != 999 && panel.iTile[i].destructible &&
                panel.iTile[i].isCorrectItem(this) && !panel.iTile[i].invincible) {
            panel.iTile[i].playSE();
            panel.iTile[i].life--;
            panel.iTile[i].invincible = true;

            // Generate particle
            generateParticle(panel.iTile[i], panel.iTile[i]);

            if (panel.iTile[i].life == 0) {
                panel.iTile[i] = panel.iTile[i].getDestroyedForm();
            }
        }
    }

    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            panel.playSE(8);
            panel.gameState = panel.dialogueState;
            panel.ui.currentDialogue = "You are level " + level +" now!\n" + "You feel stronger!";
        }
    }

    public void selectItem() {
        int itemIndex = panel.ui.getItemIndexOnSlot();

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);
            if (selectedItem.type == typeSword || selectedItem.type == typeAxe) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if (selectedItem.type == typeShield) {
                currentShield = selectedItem;
                defense = getDefense();
            }
            if (selectedItem.type == typeConsumable) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up":
                if (!attacking) {
                    if (spriteNum == 1) {image = up1;}
                    if (spriteNum == 2) {image = up2;}
                }
                if (attacking) {
                    tempScreenY = screenY - panel.tileSize;
                    if (spriteNum == 1) {image = attackUp1;}
                    if (spriteNum == 2) {image = attackUp2;}
                }
                break;
            case "down":
                if (!attacking) {
                    if (spriteNum == 1) {image = down1;}
                    if (spriteNum == 2) {image = down2;}
                }
                if (attacking) {
                    if (spriteNum == 1) {image = attackDown1;}
                    if (spriteNum == 2) {image = attackDown2;}
                }
                break;
            case "left":
                if (!attacking) {
                    if (spriteNum == 1) {image = left1;}
                    if (spriteNum == 2) {image = left2;}
                }
                if (attacking) {
                    tempScreenX = screenX - panel.tileSize;
                    if (spriteNum == 1) {image = attackLeft1;}
                    if (spriteNum == 2) {image = attackLeft2;}
                }
                break;
            case "right":
                if (!attacking) {
                    if (spriteNum == 1) {image = right1;}
                    if (spriteNum == 2) {image = right2;}
                }
                if (attacking) {
                    if (spriteNum == 1) {image = attackRight1;}
                    if (spriteNum == 2) {image = attackRight2;}
                }
                break;
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // DEBUG
//        g2.setFont(new Font("Arial", Font.PLAIN, 26));
//        g2.setColor(Color.WHITE);
//        g2.drawString("Invincible: " + invincibleCounter, 10, 400);
    }


}
