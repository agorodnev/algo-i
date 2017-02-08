import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] tries;
    private int trials;
    private double mean;
    private double deviation;
    private double sharpness;

    public PercolationStats(int n, int trials)
    {
        if (n < 1 || trials < 1)
            throw new java.lang.IllegalArgumentException();

        tries = new double[trials];
        this.trials = trials;

        while (trials > 0)
        {
            int open = 0;
            Percolation pc = new Percolation(n);
            while (!pc.percolates())
            {
                int x = StdRandom.uniform(n) + 1;
                int y = StdRandom.uniform(n) + 1;
                if (pc.isOpen(x, y))
                    continue;
                pc.open(x, y);
                open++;
            }
            trials--;
            tries[trials] = (double) open/(n*n);
        }

        mean = StdStats.mean(tries);
        deviation = StdStats.stddev(tries);
        sharpness = 1.96 * deviation/(Math.sqrt((double) this.trials));
    }
    public double mean()
    {
            return mean;
    }
    public double stddev()
    {
            return deviation;
    }
    public double confidenceLo()
    {
            return mean - sharpness;
    }
    public double confidenceHi()
    {        
            return mean + sharpness;
    }
    public static void main(String[] args)
    {
        PercolationStats ps = new PercolationStats(2, 10000);
        StdOut.println("mean\t\t\t\t\t" + ps.mean());
        StdOut.println("stddev\t\t\t\t\t" + ps.stddev());
        StdOut.println("95% confidence interval\t\t\t" + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
