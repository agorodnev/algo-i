import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;
    private List<List<Point>> groupBy(Point[] points)
    {
        List<List<Point>> results = new ArrayList<List<Point>>();
        if (points.length < 4)
            return results;
        Point pivot = points[0];
        double slope = pivot.slopeTo(points[1]);
        List<Point> segs = new ArrayList<Point>();
        segs.add(pivot);
        segs.add(points[1]);
        for (int i = 2; i < points.length; i++)
        {
            double p_slope = pivot.slopeTo(points[i]);
            if (p_slope == slope) 
            {
                segs.add(points[i]);
            } else {
                if (segs.size() > 3)
                {
                    Collections.sort(segs);
                    results.add(segs);
                }
                segs = new ArrayList<Point>();
                segs.add(pivot);
                segs.add(points[i]);
                slope = p_slope;
            }

            if (i == points.length - 1 && segs.size() > 3)
            {
                Collections.sort(segs);
                results.add(segs);
            }
        }
        return results;
    }

    public FastCollinearPoints(Point[] points)
    {
        if (points == null)
            throw new java.lang.NullPointerException();
        TreeSet<Point> ps = new TreeSet<Point>();
        Point[] aps = points.clone();
        segments = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length; i++)
        {
            if (aps[i] == null)
                throw new java.lang.NullPointerException();
            if (ps.contains(aps[i]))
                throw new java.lang.IllegalArgumentException();
            ps.add(aps[i]);
        }
        Point[] arrayOfPoints = points.clone();
        Arrays.sort(arrayOfPoints);
        Point p;
        TreeSet<List<Point>> uniqueSegments = new TreeSet<List<Point>>(); 
        TreeMap<Point, TreeSet<Point>> seenSegments = new TreeMap<Point, TreeSet<Point>>();
        for (int i = 0; i < arrayOfPoints.length; i++)
        {
            p = aps[i];
            Comparator<Point> comp = p.slopeOrder();
            Arrays.sort(arrayOfPoints, comp);
            List<List<Point>> groups = groupBy(arrayOfPoints);
            for (List<Point> seg: groups)
            {
                Point begin = seg.get(0);
                Point end = seg.get(seg.size() - 1);

                TreeSet<Point> ts = seenSegments.get(begin);
                if (ts == null)
                {
                    ts = new TreeSet<Point>();
                    ts.add(end);
                    seenSegments.put(begin, ts);
                } else {
                    if (ts.contains(end))
                    {
                        continue;
                    }
                    else
                    {
                        ts.add(end);
                    }
                }
            }
        }
        for (Map.Entry<Point, TreeSet<Point>> entry: seenSegments.entrySet())
        {
            Point begin = entry.getKey();
            TreeSet<Point> t = entry.getValue();
            for (Point end: t)
                segments.add(new LineSegment(begin, end));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments())
            StdOut.println(segment);
        for (LineSegment segment : collinear.segments()) {
            segment.draw();
        }
        StdDraw.show();
        StdOut.println(collinear.numberOfSegments());
    }
}
