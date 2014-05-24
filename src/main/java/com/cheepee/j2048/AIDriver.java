/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cheepee.j2048;

import org.openqa.selenium.Keys;

/**
 * Drivers for various AI implementations.
 * @author cipi
 */
public interface AIDriver {
    
    /**
     * Start a game.
     * @param board Initial state of the board.
     */
    public void start(int[] board);
    
    /**
     * End the game.
     */
    public void end();
    
    /**
     * Gets the next move.
     * @return Keys.LEFT, Keys.RIGHT, Keys.UP or Keys.DOWN
     */
    public CharSequence nextMove();

    /**
     * Informs the AI of the state of the board after performing its move.
     * @param aiMove The last move obtained with nextMove()
     * @param board The state of the board after the move was performed.
     */
    public void processReply(CharSequence aiMove, int[] board);
}
