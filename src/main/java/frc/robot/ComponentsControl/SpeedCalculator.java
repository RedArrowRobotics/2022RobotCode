package frc.robot.ComponentsControl;

public class SpeedCalculator {
    public double differenceInSpeed(double setTarget)
    {
        //return ( (-0.0105263158)*setTarget )+31.05263158;
        return ( (-0.0105263158)*setTarget )-8.94736842;
    }
    public double variableTarget(double distance)
    {
        //return ( (0.0000008333333333)*(distance*distance) )+( 0.01755*distance )+( -60.73666667 );
        return ( (1.352313167)*(distance*distance) )+( (90.71174377)*distance )+( 4389.092527 );
    }
}
