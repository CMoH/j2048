/*
 * Copyright (C) 2014 cipi
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.cheepee.j2048;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the state of the game board.
 *
 * @author cipi
 */
public class GameBoard {

    private ArrayList<Integer> board;

    public GameBoard() {
        this.board = new ArrayList<Integer>(16);
        for(int i = 0; i < 16; ++i) {
            board.add(0);
        }
    }

    public void setTile(int x, int y, int value) {
        board.set(x + y * 4, value);
    }

    public int getTile(int x, int y) {
        return board.get(x + y * 4);
    }
    
    public List<Integer> asList() {
        return board;
    }

    public void print(PrintStream writer) {
        for (int i = 0; i < board.size(); ++i) {
            writer.printf("%5d", board.get(i));
            if ((i + 1) % 4 == 0) {
                writer.println();
            }
        }
    }

}
