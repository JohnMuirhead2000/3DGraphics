package objects.geometry.threeD;

public class Edge3D {

    // Two points will specify a Edge.
    Point3D[] points = new Point3D[2];

    public Edge3D(Point3D p1, Point3D p2){
        points[0] = p1;
        points[1] = p2;
    }

    public Point3D[] getPoints(){return points;}

    // returns true if the argument Edge is the same as this Edge
    public boolean EdgeMatches(Edge3D Edge) {
        Point3D[] this_points = points;
        Point3D[] Edge_points = Edge.getPoints();

        return Point3D.samePoints(this_points, Edge_points);
    }
}
