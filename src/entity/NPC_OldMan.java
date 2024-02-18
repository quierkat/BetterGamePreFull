package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_OldMan extends Entity {

    public NPC_OldMan(GamePanel panel) {
        super(panel);

        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = setUp("/npc/oldman_up_1", panel.tileSize, panel.tileSize);
        up2 = setUp("/npc/oldman_up_2", panel.tileSize, panel.tileSize);
        down1 = setUp("/npc/oldman_down_1", panel.tileSize, panel.tileSize);
        down2 = setUp("/npc/oldman_down_2", panel.tileSize, panel.tileSize);
        left1 = setUp("/npc/oldman_left_1", panel.tileSize, panel.tileSize);
        left2 = setUp("/npc/oldman_left_2", panel.tileSize, panel.tileSize);
        right1 = setUp("/npc/oldman_right_1", panel.tileSize, panel.tileSize);
        right2 = setUp("/npc/oldman_right_2", panel.tileSize, panel.tileSize);
    }

    public void setDialogue() {
        dialogues[0] = "Hello, traveler.";
        dialogues[1] = "So you've come to this island to \nfind the treasure?";
        dialogues[2] = "I used to be a great wizard, but now... \nI'm a bit too old for taking on an adventure.";
        dialogues[3] = "Well, good luck on your quest!";
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
    }

    public void speak() {
        super.speak();
    }

}
