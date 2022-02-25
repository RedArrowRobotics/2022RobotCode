package frc.robot.ComponentsControl;

public class PIDCalculator {
    public ShotParameters calculateShot(double rangeToTarget, int shotType)
    {
        ShotParameters shotParameters = new ShotParameters();
        
        switch (shotType)
        {
            case 0:
                shotParameters.P = 0.0;
                shotParameters.I = 0.0;
                shotParameters.D = 0.0;
                shotParameters.FF = 0.0;
                shotParameters.targetVelocity = 3000;
                break;
            case 1:
                shotParameters.P = 0.0;
                shotParameters.I = 0.0;
                shotParameters.D = 0.0;
                shotParameters.FF = 0.0;
                shotParameters.targetVelocity = 5600;
                break;
            case 2:
                shotParameters.P = 0.0;
                shotParameters.I = 0.0;
                shotParameters.D = 0.0;
                shotParameters.FF = 0.0;
                shotParameters.targetVelocity = 0;
                break;
            default:
                break;
        }
        return shotParameters;
    }    
}
