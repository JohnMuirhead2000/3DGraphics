package objects.geometry.threeD;

public class Point3D {

    // a points in space will be an array [x, y, z]
    double[] coordinates = new double[3];
    int ID;

    public Point3D(int ID, double x, double y, double z){
        this.ID = ID;
        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;
    }
    public double[] getCoordinates(){return coordinates;}
    public int getID(){return ID;}



    // Given 2 arrays array of 2 points, determines if they are the same two points
    public static boolean samePoints(Point3D[] p1, Point3D[] p2){
        Point3D p1_1 = p1[0];
        Point3D p1_2 = p1[1];

        Point3D p2_1 = p2[0];
        Point3D p2_2 = p2[1];

        return (((p1_1.getID() == p2_1.getID()) && (p1_2.getID() == p2_2.getID())) ||
                ((p1_1.getID() == p2_2.getID()) && (p1_2.getID() == p2_1.getID())));
    }

    // updates the Point3D coordinate points
    public void updateCoords(double newCoords[]){
        coordinates[0] = newCoords[0];
        coordinates[1] = newCoords[1];
        coordinates[2] = newCoords[2];

    }

    // determines the Point3D distance to the origin
    public double getDistanceToOrigin(){
        return java.lang.Math.sqrt(coordinates[0] * coordinates[0] +
                coordinates[1] * coordinates[1] +
                coordinates[2] * coordinates[2]);
    }
}
