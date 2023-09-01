package objects.geometry.threeD;

import objects.math.RotationMatrix;
import objects.math.TranslationMatrix;

import java.util.ArrayList;
import java.util.List;

// This class specifies a polygon generated via the text file. The data is how it would be represented in 3D
public class Polygon3D {

    List<Point3D> points = new ArrayList<>();
    List<Edge3D> edges = new ArrayList<>();
    List<Face3D> faces = new ArrayList<>();

    public Polygon3D(){}

    // given two points, adds the edge they form if it is not already in the list of verticies
    public void addEdge(Point3D p1, Point3D p2){
        Edge3D edge = new Edge3D(p1, p2);
        if (!EdgeAlreadyIncluded(edge)){
            edges.add(edge);
        }
    }

    // adds point to points. Assumes point is not already in points.
    public void addPoint(Point3D point){points.add(point);}

    public void addFace(int index, Point3D p1, Point3D p2, Point3D p3){
        Face3D face = new Face3D(index, p1, p2, p3);
        faces.add(face);
    }


    public List<Point3D> getPoints(){return points;}
    public List<Edge3D> getedges(){return edges;}
    public List<Face3D> getFaces(){return faces;}

    private boolean EdgeAlreadyIncluded(Edge3D edge){
        for (Edge3D currentEdge : edges) {
            if (currentEdge.EdgeMatches(edge)) {
                return true;
            }
        }
        return false;
    }

    // given the id of point being dragged and its new position, update its own position and every other points:
    public void modify_pos(double[] new_pos, int id){

        Point3D pointBeingDragged = getPointFromId(id);

        // temp point has 0 no ID, but it is in the soon-to-be posiition of the Point3D with this.ID = id
        Point3D tempPoint = new Point3D(0, new_pos[0], new_pos[1], new_pos[2]);

        RotationMatrix rotationMatrix = new RotationMatrix(pointBeingDragged, tempPoint);

        // update the points in accordance to the rotation matrix
        for (Point3D point : points) {
            rotationMatrix.performRotation(point);
        }
    }

    // this function updates the faces with the most up-to-date information in this Polygon3D
    public void fixFaces(){
        for (int i = 0; i < faces.size(); i++) {
            Face3D face = faces.get(i);

            Point3D p1 = face.getPoints()[0];
            Point3D p2 = face.getPoints()[1];
            Point3D p3 = face.getPoints()[2];

            double[] newValues = face.getAngleAndAverageZFrom3Points(p1, p2, p3);

            face.setAngleWithZ(newValues[0]);
            face.setAverageZ(newValues[1]);
        }
    }


    // done only once. Function scales the polygon (while preserving proportions of course) and
    // translates its center to the origin [0, 0, 0]
    public void scalePolygon(){
        double totalX = 0;
        double totalY = 0;
        double totalZ = 0;

        for (Point3D point : points){
            totalX = totalX + point.getCoordinates()[0] ;
            totalY = totalY + point.getCoordinates()[1] ;
            totalZ = totalZ + point.getCoordinates()[2] ;
        }
        double centerX = totalX / points.size();
        double centerY = totalY / points.size();
        double centerZ = totalZ / points.size();

        Point3D originPoint = new Point3D(0, 0, 0, 0);
        Point3D centerPoint = new Point3D(0, centerX, centerY, centerZ);

        // translate the shape such that its center matches the origin
        TranslationMatrix translationMatrix = new TranslationMatrix(centerPoint, originPoint);
        for (Point3D point : points){
            translationMatrix.performTranslation(point);}

        // scale it down
        Point3D furtherstPoint = getFurthest();
        double distance = furtherstPoint.getDistanceToOrigin();
        double scalingFactor = distance / 2; // the furthest point will be scaled to being 2 away
        for (Point3D point : points){
            double[] newCoords = new double[3];
            newCoords[0] = point.getCoordinates()[0] / scalingFactor;
            newCoords[1] = point.getCoordinates()[1] / scalingFactor;
            newCoords[2] = point.getCoordinates()[2] / scalingFactor;
            point.updateCoords(newCoords);
        }
    }

    // given an ID, finds the point (if it exists) in the polygon with the matching ID
    public Point3D getPointFromId(int ID){
        for (Point3D point : points) {
            if (point.getID() == ID) {
                return point;
            }
        }
        return null;
    }

    // finds the Point3D in the polygon closest to the observer just in terms of z value
    public Point3D getCloses() {
        Point3D max = points.get(0);
        for (int i = 1; i < points.size(); i++){
            if (points.get(i).getCoordinates()[2] > max.getCoordinates()[2]){
                max = points.get(i);
            }
        }
        return max;
    }

    // finds the Point3D in the polygon furthest from the origin
    public Point3D getFurthest() {
        Point3D max = points.get(0);
        for (int i = 1; i < points.size(); i++){
            if (points.get(i).getDistanceToOrigin() > max.getDistanceToOrigin()){
                max = points.get(i);
            }
        }
        return max;
    }
}
