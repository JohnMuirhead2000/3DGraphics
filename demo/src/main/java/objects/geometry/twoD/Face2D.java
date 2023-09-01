package objects.geometry.twoD;

import objects.geometry.threeD.Face3D;
import objects.geometry.threeD.Point3D;

public class Face2D{

    int ID;
    Point2D[] points = new Point2D[3];
    double angleWithZAxis;
    double averageZ;

    public Face2D(int id, Point2D p1, Point2D p2, Point2D p3, double angleWithZAxis, double averageZ){
        ID = id;
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;

        this.angleWithZAxis = angleWithZAxis;
        this.averageZ = averageZ;
    }

    public double getAverageZ(){return averageZ;}
    public double getAngleWithZAxis(){return angleWithZAxis;}
    public Point2D[] getPoints(){return points;}
}
