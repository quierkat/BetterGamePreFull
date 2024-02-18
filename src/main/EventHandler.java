package main;

import java.awt.*;

public class EventHandler {

    GamePanel panel;
    EventRect eventRect[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel panel) {
        this.panel = panel;

        eventRect = new EventRect[panel.maxWorldCols][panel.maxWorldRows];

        int col = 0;
        int row = 0;
        while (col < panel.maxWorldCols && row < panel.maxWorldRows) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == panel.maxWorldCols) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {

        // Check if the player character is more that 1 tile away from the last event
        int xDistance = Math.abs(panel.player.worldX - previousEventX);
        int yDistance = Math.abs(panel.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > panel.tileSize) {
            canTouchEvent = true;
        }

        if (canTouchEvent) {
            //        if (hit(27, 16, "right")) {teleport(panel.dialogueState);}
            if (hit(27, 16, "right")) {damagePit(27, 16, panel.dialogueState);}
            if (hit(23, 19, "any")) {damagePit(23, 19, panel.dialogueState);}
            if (hit(23, 12, "up")) {healingPool(23, 12, panel.dialogueState);}
        }

    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        panel.player.solidArea.x = panel.player.worldX + panel.player.solidArea.x;
        panel.player.solidArea.y = panel.player.worldY + panel.player.solidArea.y;
        eventRect[col][row].x = col * panel.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * panel.tileSize + eventRect[col][row].y;

        if (panel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (panel.player.direction.equals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;

                previousEventX = panel.player.worldX;
                previousEventY = panel.player.worldY;
            }
        }

        panel.player.solidArea.x = panel.player.solidAreaDefaultX;
        panel.player.solidArea.y = panel.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void teleport(int gameState) {
        panel.gameState = gameState;
        panel.ui.currentDialogue = "Teleport!";
        panel.player.worldX = panel.tileSize*37;
        panel.player.worldY = panel.tileSize*10;
    }

    public void damagePit(int col, int row, int gameState) {
        panel.gameState = gameState;
        panel.playSE(6);
        panel.ui.currentDialogue = "You fall into a pit!";
        panel.player.life -= 1;
        canTouchEvent = false;
    }

    public void healingPool(int col, int row, int gameState) {
        if (panel.keyHandler.enterPressed) {
            panel.gameState = gameState;
            panel.player.attackCanceled = true;
            panel.playSE(2);
            panel.ui.currentDialogue = "You drink the water.\nYou feel revitalized.";
            panel.player.life = panel.player.maxLife;
            panel.player.mana = panel.player.maxMana;
            panel.aSetter.setMonster();
        }
    }

}
