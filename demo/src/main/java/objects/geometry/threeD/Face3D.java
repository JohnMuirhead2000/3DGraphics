package objects.geometry.threeD;

import objects.math.RotationMatrix;

public class Face3D {

    int id;
    Point3D[] points = new Point3D[3];
    double angleWithZ;
    double averageZ;

    public Face3D(int ID, Point3D p1, Point3D p2, Point3D p3){
        id = ID;
        double[] instanceValues = getAngleAndAverageZFrom3Points(p1, p2, p3);
        angleWithZ = instanceValues[0];
        averageZ = instanceValues[1];
    }

    // given 3 points, finds both the anlge that face would make with the z axis and the average z position
    public double[] getAngleAndAverageZFrom3Points(Point3D p1, Point3D p2, Point3D p3){
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;

        //obtain two vectors [x, y, z] do define the plane that is this face
        double[] vector1 = vectorFromPoints(p1, p2);
        double[] vector2 = vectorFromPoints(p1, p3);

        double[] orthogonalVector = RotationMatrix.crossProductArrays(vector1, vector2);

        //the dot product with the k unit vector [0, 0, 1]
        double dotProduct = orthogonalVector[2];
        double cosOfAngle = dotProduct / RotationMatrix.magnitudeArray(orthogonalVector);

        angleWithZ = Math.acos(cosOfAngle);
        averageZ = (p1.getCoordinates()[2] + p2.getCoordinates()[2] + p3.getCoordinates()[2]) / 3;

        double[] newValues = new double[2];
        newValues[0] = angleWithZ;
        newValues[1] = averageZ;
        return newValues;
    }

    // finds the vector which is the difference between p2 and p1
    private double[] vectorFromPoints(Point3D p1, Point3D p2) {
        double[] p1coords = p1.getCoordinates();
        double[] p2coords = p2.getCoordinates();

        double[] differenceVector = new double[3];
        differenceVector[0] = p2coords[0] - p1coords[0];
        differenceVector[1] = p2coords[1] - p1coords[1];
        differenceVector[2] = p2coords[2] - p1coords[2];
        return differenceVector;
    }

    public Point3D[] getPoints(){return points;};
    public int getId(){return id;}
    public double getAngleWithZ(){return angleWithZ;}
    public void setAngleWithZ(double newVal){angleWithZ = newVal;}
    public double getAverageZ(){return averageZ;}
    public void setAverageZ(double newVal){averageZ = newVal;}
}
