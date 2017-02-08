import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments;
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null)
            throw new java.lang.NullPointerException();
        TreeSet<Point> ps = new TreeSet<Point>();
        segments = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length; i++)
        {
            if (points[i] == null)
                throw new java.lang.NullPointerException();
            if (ps.contains(points[i]))
                throw new java.lang.IllegalArgumentException();
            ps.add(points[i]);
        }
        boolean collinear = false;
        TreeSet<Point> visited = new TreeSet<Point>();
        Arrays.sort(points);
        for (int i = 0; i < points.length; i++)
        {
            Point p = points[i];
            for (int j = i + 1; j < points.length; j++)
            {
                Point q = points[j];
                for (int k = j + 1; k < points.length; k++)
                {
                    Point r = points[k];
                    for (int f = k + 1; f < points.length; f++)
                    {
                        Point s = points[f];
                        Point[] ap = {p, q, r, s};
                        double[] slopes = new double[3];
                        slopes[0] = p.slopeTo(q);
                        slopes[1] = p.slopeTo(r);
                        slopes[2] = p.slopeTo(s);
                        if (slopes[0] == slopes[1] && slopes[0] == slopes[2])
                        {
                            if (!visited.contains(ap[0]) && !visited.contains(ap[3]))
                                segments.add(new LineSegment(ap[0], ap[3]));
                        }
                        Arrays.fill(slopes, 0);
                    }
                }
            }
        }
        
    }
    public int numberOfSegments() { return segments.size(); }
    public LineSegment[] segments() { return segments.toArray(new LineSegment[segments.size()]); }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println("Segments: " + collinear.numberOfSegments());
        StdDraw.show();
    }
}
