import java.util.ArrayList;
import java.util.Collections;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class PointSET {
    private SET<Point2D> points;
    public PointSET() {
        points = new SET<Point2D>();
    }
    public boolean isEmpty() { return points.isEmpty(); }
    public int size() { return points.size(); }
    public void insert (Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        points.add(p);
    }
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        return points.contains(p); }
    public void draw() { 
        for (Point2D p: points) {
            StdDraw.point(p.x(), p.y());
        }
    }
    public Iterable<Point2D> range(RectHV rect) 
    {
        if (rect == null)
            throw new java.lang.NullPointerException();
        ArrayList<Point2D> ps = new ArrayList<Point2D>(points.size());
        ArrayList<Point2D> pointsInRange = new ArrayList<Point2D>();
        for (Point2D p: points)
            ps.add(p);
        Collections.sort(ps, Point2D.X_ORDER);
        double x;
        double xmax = rect.xmax();
        double xmin = rect.xmin();
        for (int i = 0; i < ps.size(); i++) {
            x = ps.get(i).x();
            if (x < xmin)
                continue;
            if (x >= xmin && x <= xmax)
                pointsInRange.add(ps.get(i));
            if (x > xmax)
                break;
        }
        Collections.sort(pointsInRange, Point2D.Y_ORDER);
        double y;
        double ymax = rect.ymax();
        double ymin = rect.ymin();
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        for (int i = 0; i < pointsInRange.size(); i++) {
            y = pointsInRange.get(i).y();
            if (y < ymin)
                continue;
            if (y >= ymin && y <= ymax)
                result.add(pointsInRange.get(i));
            if (y > ymax)
                break;
        }

        return result;
    }
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new java.lang.NullPointerException();
        if (points.isEmpty())
            return null;
        Point2D nearest = points.min();
        double distance = nearest.distanceTo(p);
        double d;
        for (Point2D pt: points) {
            d = p.distanceTo(pt);
            if (d < distance) {
                distance = d;
                nearest = pt;
            }
        }

        return nearest;
    }
    public static void main(String[] args)
    {
        PointSET brute = new PointSET();
        String filename = args[0];
        StdDraw.enableDoubleBuffering();
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle
        Point2D p = new Point2D(0.500000, 0.900000);
        Point2D x = new Point2D(0.783, 0.514);
        Point2D y = new Point2D(0.775, 0.491);
        StdOut.println("distance: " + x.distanceTo(y));

        // draw the points
        p = new Point2D(0.81, 0.3);
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        Point2D nearest = brute.nearest(p);
        StdDraw.point(nearest.x(), nearest.y());
        StdOut.println("nearest to p " + p + " is " + nearest);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.line(p.x(), p.y(), nearest.x(), nearest.y());
        StdDraw.show();
    }
}
