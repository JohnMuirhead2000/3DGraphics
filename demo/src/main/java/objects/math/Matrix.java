package objects.math;

public abstract class Matrix {

    // returns the magnitude of a 3D array, represented as double[3]
    public static double magnitudeArray(double[] point){
        return java.lang.Math.sqrt(point[0] * point[0] +
                point[1] * point[1] +
                point[2] * point[2]);
    }

    // performs dot product on two vectors, represented as double[]. Assumes they are the same size
    double dotProductArrays(double[] arr1, double[] arr2){
        double sum = 0;
        for (int i = 0; i < arr1.length; i++){
            sum = sum + arr1[i] * arr2[i];
        }
        return sum;
    }

    // performs cross product on two 3D vectors, represented as double[3]s
    public static double[] crossProductArrays(double[] pStart, double[] pEnd){
        double[] result = new double[3];
        result[0] = pStart[1] * pEnd[2] - pStart[2] * pEnd[1];
        result[1] = pStart[2] * pEnd[0] - pStart[0] * pEnd[2];
        result[2] = pStart[0] * pEnd[1] - pStart[1] * pEnd[0];
        return result;
    }

    // performs matrix multiplication. returns null if this is not possible
    double[][] multiplyMatricies(double[][] matrix1, double[][] matrix2){
        double[][] finalMatrix = new double[matrix1.length][matrix2[0].length];

        // if they cannot be multiplied it just returns null
        if (matrix1[0].length != matrix2.length){
            return null;
        }

        // for each row in the first matrix
        for (int i = 0; i < matrix1.length; i++){
            for (int j = 0; j < matrix2[0].length; j++){
                double [] arrayFromM1 = matrix1[i];
                double[] arrayFromM2 = new double[matrix2.length];
                for (int k = 0; k < matrix2.length; k++){
                    arrayFromM2[k] = matrix2[k][j];
                }
                finalMatrix[i][j] = dotProductArrays(arrayFromM1, arrayFromM2);
            }
        }
        return finalMatrix;
    }

    // adds matrices, represented as double[]s
    double[][] addMatricies(double[][] matrix1, double[][] matrix2){
        double[][] finalMatrix = new double[matrix1.length][matrix1[0].length];

        // if they cannot be multiplied it just returns null
        if (matrix1[0].length != matrix2[0].length || matrix1.length != matrix2.length){
            return null;
        }

        for (int i = 0; i < matrix1.length; i++){
            for (int j = 0; j < matrix1[0].length; j++){
                finalMatrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return finalMatrix;
    }

    // multiply matrix, represented as a double[], by constant
    double[][] multiplyMatrixByConstant(double[][] matrix, double constant){
        double[][] finalMatrix = new double [matrix.length][matrix[0].length];

        for (int i = 0;  i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                finalMatrix[i][j] = matrix[i][j] * constant;
            }
        }
        return finalMatrix;
    }

    // generates the accompanying 3D unit vector for a 3D vector represented as double[3]
    double[] normalizeVector(double[] vector){
        double magnitude = magnitudeArray(vector);
        double[] finalVector = new double[3];
        finalVector[0] = vector[0]/magnitude;
        finalVector[1] = vector[1]/magnitude;
        finalVector[2] = vector[2]/magnitude;
        return finalVector;
    }}
