package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel panel;
    Graphics2D g2;
    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; // 0: first screen,  1: second screen
    public int slotCol = 0;
    public int slotRow = 0;


    public UI(GamePanel panel) {
        this.panel = panel;
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(panel);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
        Entity crystal = new OBJ_ManaCrystal(panel);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;
    }

    public void addMessage(String text) {
//        message = text;
//        messageOn = true;

        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.WHITE);

        // TITLE STATE
        if (panel.gameState == panel.titleState) {
            drawTitleScreen();
        }

        // PLAY STATE
        if (panel.gameState == panel.playState) {
            drawPlayerLife();
            drawMessage();
        }
        // PAUSE STATE
        if (panel.gameState == panel.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
        // DIALOGUE STATE
        if (panel.gameState == panel.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        if (panel.gameState == panel.characterState) {
            drawCharacterScreen();
            drawInventory();
        }
    }

    public void drawPlayerLife() {
        int x = panel.tileSize/2;
        int y = panel.tileSize/2;
        int i = 0;

        // DRAW MAX LIFE
        while (i < panel.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += panel.tileSize;
        }

        // RESET VALUES
        x = panel.tileSize/2;
        y = panel.tileSize/2;
        i = 0;

        // DRAW CURRENT LIFE
        while (i < panel.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < panel.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += panel.tileSize;
        }

        // DRAW MAX MANA
        x = (panel.tileSize/2) - 5;
        y = (int)(panel.tileSize*1.5);
        i = 0;
        while (i < panel.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        // DRAW MANA
        x = (panel.tileSize/2) - 5;
        y = (int)(panel.tileSize*1.5);
        i = 0;
        while (i < panel.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }

    public void drawMessage() {
        int messageX = panel.tileSize;
        int messageY = panel.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                g2.setColor(Color.BLACK);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.WHITE);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTitleScreen() {

        if (titleScreenState == 0) {
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, panel.screenWidth, panel.screenHeight);

            // TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "First Adventures";
            int x = getXForCenteredText(text);
            int y = panel.tileSize * 3;

            // SHADOW
            g2.setColor(Color.GRAY);
            g2.drawString(text, x+5, y+5);
            // MAIN COLOR
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

            // CHARACTER IMAGE
            x = panel.screenWidth/2 - (panel.tileSize*2)/2;
            y += panel.tileSize * 2;
            g2.drawImage(panel.player.down1, x, y, panel.tileSize*2, panel.tileSize*2, null);

            // MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NEW GAME";
            x = getXForCenteredText(text);
            y += panel.tileSize * 3.5;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x-panel.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXForCenteredText(text);
            y += panel.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x-panel.tileSize, y);
            }

            text = "QUIT";
            x = getXForCenteredText(text);
            y += panel.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x-panel.tileSize, y);
            }
        } else if (titleScreenState == 1) {
            // CLASS SELECTION SCREEN
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select your class!";
            int x = getXForCenteredText(text);
            int y = panel.tileSize*3;
            g2.drawString(text, x, y);

            text = "Fighter";
            x = getXForCenteredText(text);
            y += panel.tileSize*3;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x-panel.tileSize, y);
            }

            text = "Thief";
            x = getXForCenteredText(text);
            y += panel.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x-panel.tileSize, y);
            }

            text = "Sorcerer";
            x = getXForCenteredText(text);
            y += panel.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x-panel.tileSize, y);
            }

            text = "Back";
            x = getXForCenteredText(text);
            y += panel.tileSize*2;
            g2.drawString(text, x, y);
            if (commandNum == 3) {
                g2.drawString(">", x-panel.tileSize, y);
            }
        }

    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = panel.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen() {
        // WINDOW
        int x = panel.tileSize * 2;
        int y = panel.tileSize / 2;
        int width = panel.screenWidth - (panel.tileSize * 4);
        int height = panel.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
        x += panel.tileSize;
        y += panel.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }

    }

    public void drawCharacterScreen() {
        // CREATE A FRAME
        final int frameX = panel.tileSize;
        final int frameY = panel.tileSize;
        final int frameWidth = panel.tileSize * 5;
        final int frameHeight = panel.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + panel.tileSize;
        final int lineHeight = 35;

        // NAMES
        g2.drawString("Level", textX, textY); textY += lineHeight;
        g2.drawString("Life", textX, textY); textY += lineHeight;
        g2.drawString("Mana", textX, textY); textY += lineHeight;
        g2.drawString("Strength", textX, textY); textY += lineHeight;
        g2.drawString("Dexterity", textX, textY); textY += lineHeight;
        g2.drawString("Attack", textX, textY); textY += lineHeight;
        g2.drawString("Defense", textX, textY); textY += lineHeight;
        g2.drawString("XP", textX, textY); textY += lineHeight;
        g2.drawString("Next Level", textX, textY); textY += lineHeight;
        g2.drawString("Coins", textX, textY); textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY); textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY); textY += lineHeight;

        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        // Reset textY
        textY = frameY + panel.tileSize;
        String value;

        value = String.valueOf(panel.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.life + "/" + panel.player.maxLife);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.mana + "/" + panel.player.maxMana);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(panel.player.coins);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(panel.player.currentWeapon.down1, tailX - panel.tileSize, textY-24, null);
        textY += panel.tileSize;
        g2.drawImage(panel.player.currentShield.down1, tailX - panel.tileSize, textY-24, null);
    }

    public void drawInventory() {
        // FRAME
        int frameX = panel.tileSize * 9;
        int frameY = panel.tileSize;
        int frameWidth = panel.tileSize * 6;
        int frameHeight = panel.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = panel.tileSize + 3;

        // DRAW PLAYER'S ITEMS
        for (int i = 0; i < panel.player.inventory.size(); i++) {

            // EQUIP CURSOR
            if (panel.player.inventory.get(i) == panel.player.currentWeapon ||
                    panel.player.inventory.get(i) == panel.player.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, panel.tileSize, panel.tileSize, 10, 10);
            }

            g2.drawImage(panel.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // CURSOR
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = panel.tileSize;
        int cursorHeight = panel.tileSize;

        // DRAW CURSOR
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = panel.tileSize * 3;

        //DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + panel.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();
        if (itemIndex < panel.player.inventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for (String line : panel.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot() {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public int getXForCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = panel.screenWidth/2 - length/2;
        return x;
    }

    public int getXForAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

}
