/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cheepee.j2048;

/**
 * Drivers for various AI implementations.
 * @author cipi
 */
public interface AIDriver {
    
    /**
     * Start a game.
     * @param board Initial state of the board.
     * @throws com.cheepee.j2048.AIException
     */
    public void start(GameBoard board) throws AIException;
    
    /**
     * End the game.
     * @throws com.cheepee.j2048.AIException
     */
    public void end() throws AIException;
    
    /**
     * Gets the next move.
     * @return Keys.LEFT, Keys.RIGHT, Keys.UP or Keys.DOWN
     * @throws com.cheepee.j2048.AIException
     */
    public CharSequence nextMove() throws AIException;

    /**
     * Informs the AI of the state of the board after performing its move.
     * @param aiMove The last move obtained with nextMove()
     * @param board The state of the board after the move was performed.
     * @throws com.cheepee.j2048.AIException
     */
    public void setBoardState(CharSequence aiMove, GameBoard board) throws AIException;
}
