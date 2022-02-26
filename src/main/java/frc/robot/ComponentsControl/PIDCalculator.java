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
                shotParameters.FF = 0.0000430000072862021;
                shotParameters.targetVelocity = 2800;
                break;
            case 1:
                shotParameters.P = 0.0;
                shotParameters.I = 0.0;
                shotParameters.D = 0.0;
                shotParameters.FF = 0.000096;
                shotParameters.targetVelocity = 5800;
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
