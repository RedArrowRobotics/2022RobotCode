package frc.robot.Autonomous;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;

import frc.robot.Components;
import frc.robot.DriveTrain;
import frc.robot.SensorInputs;

public class AutonomousActionCaptureBall extends AutonomousAction {
    private double startRightPosition; 
    private double startLeftPosition;
    private double endRightPosition;
    private double endLeftPosition;

    @Override
    public void Initialize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        startRightPosition = driveTrain.getFrontRightPosition();
        startLeftPosition = driveTrain.getFrontLeftPosition();
        
        endRightPosition = startRightPosition + 30;
        endLeftPosition = startLeftPosition + 30;
        components.intakeArmControl.set(true);
    }

    @Override
    public boolean Execute(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.compressor.disable();
        if ( ( (driveTrain.getFrontRightPosition() > endRightPosition) &&
               (driveTrain.getFrontLeftPosition() > endLeftPosition) ) ||
            sensors.lowerBallPresent) 
        {
            return true;
        }
        components.intakeArmControl.set(true); 
        driveTrain.arcadeDrive(0.50, 0.0);
        components.intakeRollerMotor.set(-1.0);
        
        components.intakeBeltMotor.set(TalonSRXControlMode.PercentOutput,
            !sensors.lowerBallPresent ? 1.0 : 0.0);
        
        return false;
    }

    @Override
    public void Finalize(DriveTrain driveTrain, Components components, SensorInputs sensors) {
        components.intakeArmControl.set(false);
        components.intakeRollerMotor.set(0.0);
        components.intakeBeltMotor.set(TalonSRXControlMode.PercentOutput, 0.0);
        driveTrain.arcadeDrive(0.0, 0.0);
    }
    
}
