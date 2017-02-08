import java.util.HashMap;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.ArrayList;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private Iterable<Board> boards;
    private int  moves;
    private boolean solvable = false;
    private boolean twin_solvable = false;
    private MinPQ<Integer> pq;
    private MinPQ<Integer> twinPQ;
    private HashMap<Integer, Board> hm;
    private SearchNode solution;
    private class SearchNode implements Comparable {
        public Board node;
        public int weight;
        public int moves;
        public SearchNode parent;
        SearchNode(Board b, int m) {
            node = b;
            weight = b.manhattan() + m;
            moves = m;
        }

        public int compareTo(Object that) {
            SearchNode n = (SearchNode) that;
            return weight - n.weight;
        }
    }

    private class BoardWrapper implements Comparable{
        public Board board;

        public BoardWrapper(Board b) {
            board = b;
        }

        public int compareTo(Object that) {
            BoardWrapper b = (BoardWrapper) that;
            return board.manhattan() - b.board.manhattan();
        }

    }
    private class Table {
        private ArrayList<Board>[] table;
        private int len;
        public Table (int n) {
            table = (ArrayList<Board>[])new ArrayList[n*n];
            len = 0;
        }
        public void add(Board board) {
            int w = board.hamming();
            ArrayList<Board> boards = table[w];
            boolean found = false;
            if (boards == null)
                boards = new ArrayList<Board>();
            for (Board b: boards)
                if (board.equals(b))
                    return;

            boards.add(board);
            len++;
            table[w] = boards;
        }

        public boolean contains(Board board) {
            int w = board.hamming();
            ArrayList<Board> boards = table[w];
            if (boards == null)
                return false;
            for (Board b: boards)
                if (board.equals(b))
                    return true;
            return false;
        }

        public int size() { return len; }
    }
    public Solver(Board initial) {
        moves = 0;
        HashSet<SearchNode> closed = new HashSet<SearchNode>();
        MinPQ<SearchNode> open = new MinPQ<SearchNode>();
        SearchNode sn = new SearchNode(initial, moves);
        sn.parent = null;
        Board board = initial;
        open.insert(sn);
        int counter = 0;
        while (!open.isEmpty()) {
            sn = open.delMin();
            board = sn.node;
            moves = sn.moves;
            
            if (board.isGoal()) {
                solvable = true;
                solution = sn;
                break;
            }
            if (closed.contains(sn))
                continue;
            closed.add(sn);
            Board parent = null;
            if (sn.parent != null)
              parent = sn.parent.node;
            for (Board neighbor: board.neighbors()) {
                if (neighbor.equals(parent))
                    continue;
                SearchNode nb = new SearchNode(neighbor, moves+1);
                nb.parent = sn;
                open.insert(nb);
            }
        }
        ArrayList<Board> sol = new ArrayList<Board>();
        if (isSolvable()) {
            while (solution.parent != null) {
                sol.add(solution.node);
                solution = solution.parent;
            }
            boards = sol;
        }
    }
    public boolean isSolvable() { 
        if (solvable)
            return solvable;
        return solvable && twin_solvable;
    }
    public int moves() { return moves; }
    public Iterable<Board> solution() { return boards; }
    public static void main(String[] args)
    {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
