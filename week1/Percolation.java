import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Percolation {
    private int dimension;
    private boolean[] sites;
    private WeightedQuickUnionUF uf;

    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
        if (n < 1)
        {
            throw new java.lang.IllegalArgumentException();
        }
        dimension = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        sites = new boolean[n * n + 2];
        for (int i = 0; i < sites.length; i++)
            sites[i] = false; // blocked;

        sites[0] = true;
        sites[n * n + 1] = true;
    }
    public void open(int row, int col) // open site if it is not open already
    {
        int p = xyTo1D(row, col);

        if (sites[p])
            return;

        if (row == 1)
          uf.union(p, 0); 

        if (row == dimension)
          uf.union(p, dimension*dimension + 1);

        sites[p] = true;
        int tmp = row - 1;
        if (tmp >= 1 && isOpen(tmp, col))
        {
            uf.union(p, xyTo1D(tmp, col));
        }
        
        tmp = row + 1;
        if (tmp <= dimension && isOpen(tmp, col))
        {
            uf.union(p, xyTo1D(tmp, col));
        }

        tmp = col - 1;
        if (tmp >= 1 && isOpen(row, tmp))
        {
            uf.union(p, xyTo1D(row, tmp));
        }

        tmp = col + 1;
        if (tmp <= dimension && isOpen(row, tmp))
        {
            uf.union(p, xyTo1D(row, tmp));
        }
    }
    public boolean isOpen(int row, int col) // site can be either open or blocked
    {
        validate(row, col);
        return sites[xyTo1D(row, col)];
    }
    public boolean isFull(int row, int col) // there is a path from top to the site
    {
        int p = xyTo1D(row, col);
        return uf.connected(0, p);
    }
    public boolean percolates() // there is a full site in the bottom 
    {
        return uf.connected(0, dimension*dimension+1);
    }

    private int xyTo1D(int x, int y)
    {
        validate(x, y);
        return 1 + (x - 1) * dimension + y - 1;
    }

    private void validate(int x, int y)
    {
        if (x < 1 || y < 1)
        {
            throw new java.lang.IndexOutOfBoundsException();
        }

        if (x - 1 >= dimension || y - 1 >= dimension)
        {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public static void main(String[] args)
    {
        int n = StdIn.readInt();
        Percolation e = new Percolation(n);
        while (!StdIn.isEmpty())
        {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            e.open(p, q);
            if (e.percolates())
            {
                StdOut.println("Percolates!");
                break;
            }
        }
    }
}
