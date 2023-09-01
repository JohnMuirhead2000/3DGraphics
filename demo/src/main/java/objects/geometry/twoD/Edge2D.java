package objects.geometry.twoD;

public class Edge2D {

    Point2D[] points = new Point2D[2];

    public Edge2D(Point2D p1, Point2D p2){
        points[0] = p1;
        points[1] = p2;
    }
    public Point2D[] getPoints(){return points;}
}
