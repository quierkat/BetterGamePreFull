package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel panel;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;

    //DEBUG
    public boolean checkDrawTime;

    public KeyHandler(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}  // Unused

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if (panel.gameState == panel.titleState) {
            titleState(code);
        }

        // PLAY STATE
        else if (panel.gameState == panel.playState) {
            playState(code);
        }

        // PAUSE STATE
        else if (panel.gameState == panel.pauseState) {
            pauseState(code);
        }

        // DIALOGUE STATE
        else if (panel.gameState == panel.dialogueState) {
            dialogueState(code);
        }

        // CHARACTER STATE
        else if (panel.gameState == panel.characterState) {
            characterState(code);
        }
    }

    public void titleState(int code) {
        if (panel.ui.titleScreenState == 0) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 2) {
                    panel.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (panel.ui.commandNum == 0) {
                    panel.ui.titleScreenState = 1;
                }
                if (panel.ui.commandNum == 1) {
                    // add later
                }
                if (panel.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }
        else if (panel.ui.titleScreenState == 1) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 3;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 3) {
                    panel.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (panel.ui.commandNum == 0) {
                    System.out.println("Do some fighter specific stuff.");
                    panel.gameState = panel.playState;
//                        panel.playMusic(0); // TODO: turn this back on when it is released
                }
                if (panel.ui.commandNum == 1) {
                    System.out.println("Do some thief specific stuff.");
                    panel.gameState = panel.playState;
                    panel.playMusic(0);
                }
                if (panel.ui.commandNum == 2) {
                    System.out.println("Do some sorcerer specific stuff.");
                    panel.gameState = panel.playState;
                    panel.playMusic(0);
                }
                if (panel.ui.commandNum == 3) {
                    panel.ui.titleScreenState = 0;
                    panel.ui.commandNum = 0; // outside of tutorial
                }
            }
        }
    }

    public void playState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_P) {
            panel.gameState = panel.pauseState;
        }
        if (code == KeyEvent.VK_C) {
            panel.gameState = panel.characterState;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }

        //DEBUG
        if (code == KeyEvent.VK_T) {
            if (!checkDrawTime){
                checkDrawTime = true;
            } else if (checkDrawTime) {
                checkDrawTime = false;
            }
        }
    }

    public void pauseState(int code) {
        if (code == KeyEvent.VK_P){
            panel.gameState = panel.playState;
        }
    }

    public void dialogueState(int code) {
        if (code == KeyEvent.VK_ENTER) {
            panel.gameState = panel.playState;
        }
    }

    public void characterState(int code) {
        if (code == KeyEvent.VK_C) {
            panel.gameState = panel.playState;
        }
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (panel.ui.slotRow != 0) {
                panel.ui.slotRow--;
                panel.playSE(9);
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (panel.ui.slotCol != 0) {
                panel.ui.slotCol--;
                panel.playSE(9);
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (panel.ui.slotRow != 3) {
                panel.ui.slotRow++;
                panel.playSE(9);
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (panel.ui.slotCol != 4) {
                panel.ui.slotCol++;
                panel.playSE(9);
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            panel.player.selectItem();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false; // Outside of tutorial. Interacting doesn't work great
        }
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }
}
