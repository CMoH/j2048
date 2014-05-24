/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cheepee.j2048;

import org.openqa.selenium.Keys;

/**
 * AI that replies with random moves.
 * 
 * Used in testing the browser driver.
 * 
 * @author cipi
 */
public class RandomAIDriver implements AIDriver {

    public void start(GameBoard board) throws AIException {
        // ignored
    }

    public void end() throws AIException {
        // ignored
    }

    public CharSequence nextMove() throws AIException {
        double rnd = Math.random() * 4;
        if (rnd < 1.0) return Keys.LEFT;
        else if (rnd < 2.0) return Keys.RIGHT;
        else if (rnd < 3.0) return Keys.UP;
        else return Keys.DOWN;
    }

    public void setBoardState(CharSequence aiMove, GameBoard board) throws AIException {
        // ignored
    }
    
}
