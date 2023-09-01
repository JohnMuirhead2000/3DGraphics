package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import objects.geometry.threeD.Point3D;
import objects.geometry.threeD.Polygon3D;
import objects.geometry.twoD.Face2D;
import objects.geometry.twoD.Point2D;
import objects.geometry.twoD.Polygon2D;
import objects.geometry.twoD.Edge2D;

public class graphicsApplication extends Application {


    //globals required to make the application function
    Polygon3D mainPolygon3D;
    Polygon2D mainPolygon2D;
    Stage mainStage;
    Scene mainScene;
    Point3D closestPoint; //closest to the computer screen that is
    Point3D furtherPoint; //furthest form the origin. could very well be closestPoint
    double[] prevMousePos = new double[2];
    double SCREEN_WIDTH = 2000;
    double SCREEN_HEIGHT = 1000;
    static String filePath;



    // start file called from launch.
    @Override
    public void start(Stage stage) throws IOException {

        mainStage = stage;
        // first step is to generate a 3D Polygon from the object.txt file
        mainPolygon3D = build3DPolygon();
        closestPoint = mainPolygon3D.getCloses();
        furtherPoint = mainPolygon3D.getFurthest();

        // from the 3D Polygon we can create a 2D polygon object
        mainPolygon2D = new Polygon2D(mainPolygon3D, furtherPoint);
        Pane pane = buildPane();

        // create scene and display it
        mainScene = new Scene(pane, SCREEN_WIDTH, SCREEN_HEIGHT);
        mainStage.setTitle("Graphics Application");
        mainStage.setScene(mainScene);
        mainStage.show();
    }


    // called once. builds the original polygon
    public Polygon3D build3DPolygon() throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        Polygon3D polygon3D = new Polygon3D();
        updatePolygon3DfromBR(polygon3D, br);
        polygon3D.scalePolygon();
        return polygon3D;
    }

    // given a 3d polygon and a BufferedReader (br) updates polygon according to the file from br
    // NOTE: the method assumes br comes from a text file with the format specified in the Assignment Document
    void updatePolygon3DfromBR(Polygon3D polygon, BufferedReader br) throws IOException {
        String firstLine = br.readLine();
        ArrayList<String> vertAndFaceAmount = new ArrayList<>(Arrays.asList(firstLine.split(",")));
        int numberOfedges = Integer.parseInt(vertAndFaceAmount.get(0));
        int numberOfFaces = Integer.parseInt(vertAndFaceAmount.get(1));

        // for each 'point' line in the text file add that point to the polygon
        for (int i = 0; i < numberOfedges; i++){
            updatePointsFromBufferReader(polygon, br);
        }
        // for each 'face' line in the text file add that point to the polygon
        for (int i = 0; i < numberOfFaces; i++){
            updateedgesFromBufferReader(polygon, br, i);
        }
    }

    // adds the line from the br to the polygon. the method assumes the current line represents a point
    void updatePointsFromBufferReader(Polygon3D polygon, BufferedReader br) throws IOException {
        String currentLine = br.readLine();
        ArrayList<String> pointLine = new ArrayList<>(Arrays.asList(currentLine.split(",")));
        int ID = Integer.parseInt(pointLine.get(0));
        double X = Double.parseDouble(pointLine.get(1));
        double Y = Double.parseDouble(pointLine.get(2));
        double Z = Double.parseDouble(pointLine.get(3));
        Point3D point = new Point3D(ID,X,Y,Z);

        //now add this point to points
        polygon.addPoint(point);
    }

    // adds the line from the br to the polygon. the method assumes the current line represents a face
    void updateedgesFromBufferReader(Polygon3D polygon, BufferedReader br, int index) throws IOException {
        String currentLine = br.readLine();
        ArrayList<String> FaceLine = new ArrayList<>(Arrays.asList(currentLine.split(",")));
        Point3D p1 = getPointFromID(Integer.parseInt(FaceLine.get(0)), polygon.getPoints());
        Point3D p2 = getPointFromID(Integer.parseInt(FaceLine.get(1)), polygon.getPoints());
        Point3D p3 = getPointFromID(Integer.parseInt(FaceLine.get(2)), polygon.getPoints());

        // we now have three connected points, forming 3 edges
        polygon.addEdge(p1, p2);
        polygon.addEdge(p1, p3);
        polygon.addEdge(p2, p3);

        polygon.addFace(index, p1, p2, p3);
    }

    // finds the Point3D matching the specified ID
    Point3D getPointFromID(int ID, List<Point3D> points){
        for (Point3D currentPoint : points) {
            if (currentPoint.getID() == ID) {
                return currentPoint;
            }
        }
        //If it cannot find the point in the specified list, it returns null
        return null;
    }

    // form the global mainPolygon2D builds the pane to display on the stage
    Pane buildPane(){
        Pane pane = new Pane();
        Circle addLast = null;
        // first put points on
        for (Point2D point : mainPolygon2D.getPoints()){
            Circle circle;
            circle = putPointOnPane(pane, point);
            if (circle != null){addLast = circle;}
        }

        for (Edge2D edge : mainPolygon2D.getEdges()){
            putEdgeOnPane(pane, edge);
        }

        //then draw faces
        for (Face2D face : mainPolygon2D.getFaces()){
            putFaceOnPane(pane, face);
        }
        addLast.toFront();
        pane.getChildren().add(addLast);
        pane.setOnMouseDragged(t -> rotate_object(t, closestPoint.getID()));
        return pane;
    }

    // puts a point on the pane unless it is the closes point, in which case it returns it
    Circle putPointOnPane(Pane pane, Point2D point){
        Circle circle = new Circle();
        double[] centerVals = graph2DToScreen(point.getX(), point.getY());
        circle.setCenterX(centerVals[0]);
        circle.setCenterY(centerVals[1]);
        circle.setRadius(point.getRelativeSize() * 10);
        if (point.getID() == closestPoint.getID()){
            circle.setFill(Color.RED);
            return circle;
        } else{
            circle.setFill(Color.BLUE);
            pane.getChildren().add(circle);
            return null;
        }
    }

    // puts an edge on the pane
    void putEdgeOnPane(Pane pane, Edge2D edge){
        double[] centerP1 = graph2DToScreen(edge.getPoints()[0].getX(), edge.getPoints()[0].getY());
        double[] centerP2 = graph2DToScreen(edge.getPoints()[1].getX(), edge.getPoints()[1].getY());
        Line line = new Line(centerP1[0], centerP1[1], centerP2[0], centerP2[1]);
        line.setStroke(Color.BLUE);
        pane.getChildren().add(line);
    }

    // puts an edge on the pane
    void putFaceOnPane(Pane pane,Face2D face){
        double angle = face.getAngleWithZAxis();
        if (angle > 3.14/2){angle = 3.14 - angle;}

        double[] screenP1 = graph2DToScreen(face.getPoints()[0].getX(), face.getPoints()[0].getY());
        double[] screenP2 = graph2DToScreen(face.getPoints()[1].getX(), face.getPoints()[1].getY());
        double[] screenP3 = graph2DToScreen(face.getPoints()[2].getX(), face.getPoints()[2].getY());


        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(screenP1[0], screenP1[1],
                screenP2[0], screenP2[1],
                screenP3[0], screenP3[1]);

        //set color of polygon
        int colorDecimal = getColorValFromAngle(angle) ;
        String colorString = "#0000" + Integer.toHexString(colorDecimal);
        polygon.setFill(Paint.valueOf(colorString));
        polygon.toFront();
        pane.getChildren().add(polygon);
    }

    // from the angle (in radians) computes the correct decimal value for the color
    // as specified in the assignment document (between #00005F and  #0000FF)
    private int getColorValFromAngle(double angle) {
         double backWards = ((angle * (160/1.57)) + 95);
         int finalVal = (int) ((-1*(backWards-160))+160);
         return finalVal;
    }

    // scales a 2d graph point to a position on the javafx screen
    double[] graph2DToScreen(double x, double y){

        double step_x = furtherPoint.getDistanceToOrigin()*75;
        double step_y = furtherPoint.getDistanceToOrigin()*75;
        double xScreen = (SCREEN_WIDTH / 2) + (step_x * x);
        double yScreen = (SCREEN_HEIGHT / 2) + (step_y * -y);
        double[] finalPoints  = new double[2];

        finalPoints[0] = xScreen;
        finalPoints[1]  = yScreen;

        return finalPoints;
    }

    // scales the javaFX screen position to a 2d graph point
    double[] screenTo2DGraph(double x, double y){

        x = x - SCREEN_WIDTH/2;
        y = y - SCREEN_HEIGHT/2;

        double step_x = furtherPoint.getDistanceToOrigin()*75;
        double step_y =  furtherPoint.getDistanceToOrigin()*75;
        double xGraph = x / step_x;
        double yGraph = y / step_y;
        double[] finalPoints  = new double[2];

        finalPoints[0] = xGraph;
        finalPoints[1]  = -yGraph;

        return finalPoints;
    }

    // rotates the shape in accordance to the mouse movements
    private void rotate_object(MouseEvent t, int id){

        //step 1 determine how much the mouse moved
        if (prevMousePos[0] == 0 && prevMousePos[1]==0){
            prevMousePos[0] = t.getX();
            prevMousePos[1] = t.getY();
            return;
        }

        double x_movement = t.getX() - prevMousePos[0];
        double y_movement = t.getY() - prevMousePos[1];

        if (x_movement == 0 && y_movement==0){return;};


        //step 2 get the new 3d position from the current mouse position
        double[] new_pos = getNewPos(x_movement, y_movement , id);

        prevMousePos[0] = t.getX();
        prevMousePos[1] = t.getY();

        if (new_pos == null){return;}

        // Step 3 update the current 3D Polygon with the new information. All points must change!
        mainPolygon3D.modify_pos(new_pos, id);
        mainPolygon3D.fixFaces();
        closestPoint = mainPolygon3D.getCloses();

        // Step 4 build a new 2D Polygon with the updated 3D polygon
        mainPolygon2D = new Polygon2D(mainPolygon3D, furtherPoint);

        // Step 5 build a new plane with the new 2D Polygon
        Pane pane = buildPane();

        // Step 6 load the new plane into the stage
        mainScene.setRoot(pane);

    }

    // given how much the mouse moved and the points ID, determine the new [x, y, z] position of the point
    private double[] getNewPos(double x_move, double y_move, int id){

        Point3D initialPoint = mainPolygon3D.getPointFromId(id);
        Point2D tempPoint = mainPolygon2D.get2DPointfrom3D(initialPoint, furtherPoint);

        double[] mouse_pos = graph2DToScreen(tempPoint.getX(), tempPoint.getY());

        double mouse_x = mouse_pos[0] + x_move;
        double mouse_y = mouse_pos[1] + y_move;


        double[] graph2dCoords = screenTo2DGraph(mouse_x, mouse_y);

        double radius  = initialPoint.getDistanceToOrigin();

        // now we have the circles position on the 2D grid. We need it in 3D;
        // if we know the radius, the x and y, there are only 2 possible [x, y, z] it could be
        return get3Dfrom2DandRadius(graph2dCoords, mainPolygon3D.getPointFromId(id).getCoordinates(), radius);
    }


    // using the 2D points position, the 3D position the point was just at, and its distance from the origin,
    // the method computes the next 3D position, [x, y, z] of the point
    private double[] get3Dfrom2DandRadius(double[] coords2d, double[] prevCoords3d, double radius){

        double z3d = prevCoords3d[2];
        if (z3d < 0){return null;}

        double x3d;
        double y3d;

        double objectCenterDistanceFromScreen = 2*furtherPoint.getDistanceToOrigin();
        double focalLenth = 3*furtherPoint.getDistanceToOrigin();;

        double subjectDistance = focalLenth + (objectCenterDistanceFromScreen - z3d);

        x3d = coords2d[0] * (subjectDistance / focalLenth);
        y3d = coords2d[1] * (subjectDistance / focalLenth);

        double argument = radius*radius - (x3d * x3d + y3d * y3d);

        if (argument< 0){return null;}

        z3d = Math.sqrt(argument);

        double[] finalPos = new double[3];
        finalPos[0] = x3d;
        finalPos[1] = y3d;
        finalPos[2] = z3d;

        return finalPos;
    }


    public static void main(String[] args) {
        filePath = args[0];
        launch();
    }
}