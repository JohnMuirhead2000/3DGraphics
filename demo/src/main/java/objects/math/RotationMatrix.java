package objects.math;

import objects.geometry.threeD.Point3D;


public class RotationMatrix extends Matrix {
    double[][] matrix;

    public RotationMatrix(Point3D pStart, Point3D pEnd){
        double[] startCoords = normalizeVector(pStart.getCoordinates());
        double[] endCoords = normalizeVector(pEnd.getCoordinates());

        double[] v = crossProductArrays(startCoords, endCoords);
        double s = magnitudeArray(v);
        double c = dotProductArrays(startCoords, endCoords);

        double[][] vMatrix = new double[3][3];
        vMatrix[0][1] = -v[2];
        vMatrix[0][2] = v[1];
        vMatrix[1][0] = v[2];
        vMatrix[1][2] = -v[0];
        vMatrix[2][0] = -v[1];
        vMatrix[2][1] = v[0];

        double[][] identityMatrix = new double[3][3];
        identityMatrix[0][0] = 1;
        identityMatrix[1][1] = 1;
        identityMatrix[2][2] = 1;

        double finalConstant = (1 - c) / (s * s);
        double[][] vMatrixSquare = multiplyMatricies(vMatrix, vMatrix);
        double[][] vMatrixSquareConstant = multiplyMatrixByConstant(vMatrixSquare, finalConstant);

        double[][] firstSum = addMatricies(identityMatrix, vMatrix);

        matrix = addMatricies(firstSum, vMatrixSquareConstant);
    }

    // transforms this point in accordance to the rotation matrix, matrix.
    public void performRotation(Point3D point){
        double px = point.getCoordinates()[0];
        double py = point.getCoordinates()[1];
        double pz = point.getCoordinates()[2];

        double[][] pointAsVector = new double[3][1];
        pointAsVector[0][0] = px;
        pointAsVector[1][0] = py;
        pointAsVector[2][0] = pz;

        double[][] newCoords = multiplyMatricies(matrix, pointAsVector);


        double[] newCoordsAsArray = new double[3];
        newCoordsAsArray[0] = newCoords[0][0];
        newCoordsAsArray[1] = newCoords[1][0];
        newCoordsAsArray[2] = newCoords[2][0];

        point.updateCoords(newCoordsAsArray);
    }

}
