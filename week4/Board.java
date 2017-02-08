import java.util.List;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;

public class Board {
    private final int[][] board;
    private final int size;
    private int gap;
    private int moves;
    public Board(int[][] blocks) {
        size = blocks.length;
        if (size < 1)
            throw new java.lang.IllegalArgumentException();
        board = new int[size][size];
        moves = 0;
        for (int i = 0; i < size; i++) {
            System.arraycopy(blocks[i], 0, board[i], 0, size);
            for (int j = 0 ; j < size; j++) 
                if (blocks[i][j] == 0) 
                    gap = i*size + j;
        }
    }
    public int dimension() { return size; }
    public int hamming() {
        int f = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] != 0 && 
                    i * size + j + 1 != board[i][j])
                        f++;
        return f;
    }
    public int manhattan() { 
        int x;
        int f = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                x = board[i][j] - 1;
                if (x == -1 || i * size + j == x)
                    continue;
                f += Math.abs(x/size - i) + Math.abs(x%size - j);
            }
        return f; 
    }
    public boolean isGoal() { return manhattan() == 0; }

    private void swap(int p1, int p2, int[][] b) {
        int tmp = b[p1/size][p1%size];
        b[p1/size][p1%size] = b[p2/size][p2%size];
        b[p2/size][p2%size] = tmp;
    }

    public Board twin() { 
        int[][] tw = new int[size][size];
        int pos1 = StdRandom.uniform(size*size);
        int pos2 = StdRandom.uniform(size*size);
        while (pos1 == gap || pos2 == gap || pos1 == pos2) {
            pos1 = StdRandom.uniform(size*size);
            pos2 = StdRandom.uniform(size*size);
        }
        for (int i = 0; i < size; i++)
           System.arraycopy(board[i], 0, tw[i], 0, size);
        swap(pos1, pos2, tw);
        return new Board(tw);
    }

    public boolean equals(Object y) { 
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;

        if (this.size != that.size)
            return false;

        if (this.hamming() != that.hamming() ||
            this.manhattan() != that.manhattan())
            return false;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board[i][j] != that.board[i][j])
                    return false;
        
        return true;
    }

    private int[][] getPlainBoard() {
        int[][] tmp = new int[size][size];
        for (int i = 0; i < size; i++)
            System.arraycopy(board[i], 0, tmp[i], 0, size);
        return tmp;
    }

    public Iterable<Board> neighbors() { 
        List<Board> l = new ArrayList<Board>();
        int[][] t = getPlainBoard();
        int pos = gap+1;

        if (pos <= size*size - 1 && pos/size == gap/size ) {
            swap(gap, pos, t);
            l.add(new Board(t));
        }
        t = getPlainBoard();
        pos = gap-1;
        if (pos >= 0 && pos/size == gap/size) {
            swap(gap, pos, t);
            l.add(new Board(t));
        }
        t = getPlainBoard();
        pos = gap - size;
        if (pos >= 0 && pos%size == gap%size) {
            swap(gap, pos, t);
            l.add(new Board(t));
        }
        t = getPlainBoard();
        pos = gap+size;
        if (pos <= size*size - 1 && pos%size == gap%size) {
            swap(gap, pos, t);
            l.add(new Board(t));
        }
        return l;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size + "\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                sb.append(" ");
                sb.append(board[i][j]);
                sb.append(" ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        Board initial1 = new Board(blocks);
        System.out.println(initial);
        StdOut.println("Manhattan: " + initial.manhattan());
        for (int i = 0; i < 1000; i++)
          if (initial.equals(initial.twin()))
            StdOut.println("Haha!");
        StdOut.println("Neighbors here:");

        for(Board b: initial.neighbors())
            StdOut.println(b + "\n" + b.manhattan());
        
    }
}
