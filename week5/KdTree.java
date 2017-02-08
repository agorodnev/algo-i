import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class KdTree {
    private static final boolean oX = true;
    private static final boolean oY = false;
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }

        public void setRect(RectHV rect) {
            this.rect = rect;
        }
        public RectHV getRect() {
            return this.rect;
        }
        public Point2D getPoint() {
            return this.p;
        }
    }
    private Node root;
    private RectHV rectangle;
    private Node subtree;
    private boolean axis;
    private int size;
    private double distance;
    private Point2D nearest;
    private Point2D queryPoint;
    private HashSet<Point2D> points = null;
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    private Node insert (Node x, Point2D p, RectHV r, boolean axis) { //adding rectangle to the node should be done somewhere else
        if (x == null) {
            return new Node(p, r); // think twice
        }
        RectHV area;
        RectHV rect;
        if (axis == oX) {
            if (p.x() < x.p.x()) {
                rect = x.getRect();
                area = new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
                x.lb = insert(x.lb, p, area, oY); 
            } else {
                rect = x.getRect();
                area = new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                x.rt = insert(x.rt, p, area, oY);
            }
        } else {
            if (p.y() < x.p.y()) {
                rect = x.getRect();
                area = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
                x.lb = insert(x.lb, p, area, oX);
            } else {
                rect = x.getRect();
                area = new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
                x.rt = insert(x.rt, p, area, oX);
            }
        }
        return x;
    }

    public void insert (Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1));
            return;
        }
        if (!contains(p)) {
            root = insert(root, p, root.getRect(), oX);
            size++;
        }
    }

    private Node select(Node x, Point2D p, boolean axis) {
        if (x == null) return null;
        if (axis == oX) {
            if (p.x() < x.p.x()) {
                x = select(x.lb, p, oY);
            } else {
                if (p.x() == x.p.x() && p.y() == x.p.y())
                    return x;
                x = select(x.rt, p, oY);
            }
        } else {
            if (p.y() < x.p.y()) {
               x = select(x.lb, p, oX);
            } else {
                if (p.x() == x.p.x() && p.y() == x.p.y())
                    return x;
                x = select(x.rt, p, oX);
            }
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        return select(root, p, oX) != null;
    }

    private void iterate(Node x, boolean axis) {
        if (x != null) {
            if (axis == oX) {
                StdDraw.setPenColor(StdDraw.RED);
                Point2D p = x.getPoint();
                RectHV r = x.getRect();
                StdDraw.line(p.x(), r.ymin(), p.x(), r.ymax());
                iterate(x.lb, oY);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                Point2D p = x.getPoint();
                RectHV r = x.getRect();
                StdDraw.line(r.xmin(), p.y(), r.xmax(), p.y());
                iterate(x.lb, oX);
            }
            if (axis == oX) {
                StdDraw.setPenColor(StdDraw.RED);
                Point2D p = x.getPoint();
                RectHV r = x.getRect();
                StdDraw.line(p.x(), r.ymin(), p.x(), r.ymax());
                iterate(x.rt, oY);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                Point2D p = x.getPoint();
                RectHV r = x.getRect();
                StdDraw.line(r.xmin(), p.y(), r.xmax(), p.y());
                iterate(x.rt, oX);
            }
        }
    }
    public void draw() { 
        iterate(root, oX);
        return;
    }

    private Node walk(Node x, boolean axis) {
        RectHV r = null;
        if (x != null) {
            r = x.getRect();
            if (!(r.xmin() <= rectangle.xmin() && r.ymin() <= rectangle.ymin() 
                            && r.xmax() >= rectangle.xmax() && r.ymax() >= rectangle.ymax()))
                    return null;
            subtree = x;
            this.axis = axis;
            if (axis == oX) {
                r = x.getRect();
                walk(x.lb, oY);
            } else {
                r = x.getRect();
                if (r.intersects(rectangle))
                    walk(x.lb, oX);
            }
            if (axis == oX) {
                r = x.getRect();
                if (r.intersects(rectangle))
                    walk(x.rt, oY);
            } else {
                r = x.getRect();
                if (r.intersects(rectangle))
                    walk(x.rt, oX);
            }
        }
        return x;
    }

    private void collect(Node x, boolean axis) {
        if (x != null) {
            Point2D p = x.getPoint();
            if (rectangle.contains(p))
                points.add(p);
            collect(x.lb, !axis);
            collect(x.rt, !axis);
        }
    }

    public HashSet<Point2D> range(RectHV rect) 
    {
        if (rect == null)
            throw new java.lang.NullPointerException();
        rectangle = rect;
        Node p = walk(root, oX);
        points = new HashSet<Point2D>();
        collect(subtree, axis);
        
        return points;
    }

    //nearest = point2d
    private void nearest(Node x, boolean axis) {
        if (x != null) {
            double d = x.p.distanceTo(queryPoint);
            if (x.getRect().distanceTo(queryPoint) >= this.distance)
                return;
            if (this.distance >= d) {
                this.distance = d;
                this.nearest = x.p;
            }
            if (axis == oX) {
                nearest(x.lb, oY);
            } else {
                nearest(x.lb, oX);
            }
            if (axis == oX) {
                nearest(x.rt, oY);
            } else {
                nearest(x.rt, oX);
            }
        }
    }

    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new java.lang.NullPointerException();
        if (root == null)
            return null;
        this.nearest = null;
        this.distance = 2;
        this.queryPoint = p;
        nearest(root, oX);
        return this.nearest;
    }
    public static void main(String[] args)
    {
        KdTree brute = new KdTree();
        String filename = args[0];
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
            StdOut.println("Size: " + brute.size());
        }

        Point2D p = new Point2D(0.81, 0.3);
        Point2D x = new Point2D(0.1, 0.9);
        if (brute.contains(p))
            StdOut.println(p + " is in the tree ");
        if (brute.contains(x))
            StdOut.println(x + " is in the tree ");

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        RectHV rect = new RectHV(0, 0, 1.0, 0.5);
        Point2D nearest = brute.nearest(p);
        if (nearest != null)
            StdOut.println("nearest to p " + p + " is " + nearest);
        else
            StdOut.println("nearest is null");
    }
}
