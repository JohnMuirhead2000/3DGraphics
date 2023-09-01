package objects.math;

import objects.geometry.threeD.Point3D;

public class TranslationMatrix extends Matrix{

    double[][] matrix;
    public TranslationMatrix(Point3D start, Point3D end){
        matrix = new double[4][4];
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[3][3] = 1;
        matrix[0][3] = end.getCoordinates()[0] - start.getCoordinates()[0];
        matrix[1][3] = end.getCoordinates()[1] - start.getCoordinates()[1];
        matrix[2][3] = end.getCoordinates()[2] - start.getCoordinates()[2];
    }

    // transforms this point in accordance to the translation matrix, matrix.
    public void performTranslation(Point3D point){
        double[][] pointAsVector = new double[4][1];
        pointAsVector[0][0] = point.getCoordinates()[0];
        pointAsVector[1][0] = point.getCoordinates()[1];
        pointAsVector[2][0] = point.getCoordinates()[2];
        pointAsVector[3][0] = 1;

        double[][] newCoords = multiplyMatricies(matrix, pointAsVector);

        double[] newCoordsAsArray = new double[3];
        newCoordsAsArray[0] = newCoords[0][0];
        newCoordsAsArray[1] = newCoords[1][0];
        newCoordsAsArray[2] = newCoords[2][0];

        point.updateCoords(newCoordsAsArray);

    }
}