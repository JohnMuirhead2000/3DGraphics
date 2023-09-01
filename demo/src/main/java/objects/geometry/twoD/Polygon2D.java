package objects.geometry.twoD;

import objects.geometry.threeD.Face3D;
import objects.geometry.threeD.Point3D;
import objects.geometry.threeD.Polygon3D;
import objects.geometry.threeD.Edge3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Polygon2D implements Comparator<Face2D> {

    List<Point2D> points = new ArrayList<>();
    List<Edge2D> edges = new ArrayList<>();

    List<Face2D> faces = new ArrayList<>();

    public Polygon2D(Polygon3D polygon3D, Point3D furthestPoint){

        for (Point3D point3D : polygon3D.getPoints()){
            Point2D point2D = get2DPointfrom3D(point3D, furthestPoint);
            points.add(point2D);
        }

        for (Edge3D edge3D : polygon3D.getedges()){
            Edge2D edge2D = get2DEdgeFrom3D(edge3D, furthestPoint);
            edges.add(edge2D);
        }

        for (Face3D face3D : polygon3D.getFaces()){
            Face2D face2D = get2dFaceFrom3D(face3D, furthestPoint);
            faces.add(face2D);
        }
    }


    // convert a 3D point in space to its 2d counterpart
    public Point2D get2DPointfrom3D(Point3D point, Point3D furthestPoint){

        double x3D = point.getCoordinates()[0];
        double y3D = point.getCoordinates()[1];
        double z3D = point.getCoordinates()[2];

        double objectCenterDistanceFromScreen = 2*furthestPoint.getDistanceToOrigin();
        double focalLenth = 3*furthestPoint.getDistanceToOrigin();

        double subjectDistance = focalLenth + (objectCenterDistanceFromScreen - z3D);

        double x;
        double y;
        double relativeSize;

        x = x3D * (focalLenth / (subjectDistance));
        y = y3D * (focalLenth / (subjectDistance));
        relativeSize = (focalLenth/subjectDistance);
        return new Point2D(point.getID(), relativeSize,x, y);
    }

    // convert a 3D Face to its 2D counterpart
    public Face2D get2dFaceFrom3D(Face3D face3d, Point3D furthestPoint){
        Point3D[] points = face3d.getPoints();
        Point2D p1_2d = get2DPointfrom3D(points[0], furthestPoint);
        Point2D p2_2d = get2DPointfrom3D(points[1], furthestPoint);
        Point2D p3_2d = get2DPointfrom3D(points[2], furthestPoint);
        return new Face2D(face3d.getId(), p1_2d, p2_2d, p3_2d, face3d.getAngleWithZ(), face3d.getAverageZ());
    }



    // convert a 3D Edge to its 2D counterpart.
    private Edge2D get2DEdgeFrom3D(Edge3D edge, Point3D closestPoint){
        Point3D[] points = edge.getPoints();
        Point3D p1 = points[0];
        Point3D p2 = points[1];
        Point2D p1_2D = get2DPointfrom3D(p1, closestPoint);
        Point2D p2_2D = get2DPointfrom3D(p2, closestPoint);

        return new Edge2D(p1_2D, p2_2D);

    }

    public List<Point2D> getPoints(){return points;}
    public List<Edge2D> getEdges(){return edges;}

    public List<Face2D> getFaces(){
        faces.sort(this::compare);
        return faces;
    }

    // override the compare method so we can get the faces from back most to front most
    @Override
    public int compare(Face2D o1, Face2D o2) {
        if (o1.getAverageZ() >= o2.getAverageZ()){
            return 1;
        } else {
            return -1;
        }
    }
}
