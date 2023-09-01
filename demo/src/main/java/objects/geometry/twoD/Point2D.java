package objects.geometry.twoD;


public class Point2D {
    double x;
    double y;
    int ID;
    double relativeSize = 1;

    public Point2D(int ID, double relativeSize, double x, double y){
        this.ID = ID;
        this.relativeSize = relativeSize;
        this.x = x;
        this.y = y;
    }

    public double getX(){return x;}
    public double getY(){return y;}

    public int getID(){return ID;}
    public double getRelativeSize(){return relativeSize;}
}
