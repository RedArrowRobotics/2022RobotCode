package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;

public class ComponentsControlV1 extends ComponentsControl {

    @Override
    public void runComponents(ControlInputs controlInputs, SensorInputs sensorInputs) {
        
        DoubleSolenoid.Value solenoidPosition = 
        controlInputs.deployIntake ? 
        DoubleSolenoid.Value.kForward : 
        DoubleSolenoid.Value.kReverse;
    
        Double intakeRollerMotorPower = controlInputs.runIntake ? 1.0 : 0.0;
        Double intakeBeltMotorPower = controlInputs.runIntake ? 1.0 : 0.0;
        Double transferBeltMotorPower = 0.0;

        if ( controlInputs.shootLow || controlInputs.shootHigh)
        {
            final double lowShotTargetVelocity = 3000;
            final double highShotTargetVelocity = 5000;
            double targetVelocity = 0;
            if (controlInputs.shootLow)
            {
                targetVelocity = lowShotTargetVelocity;
            }
            if (controlInputs.shootHigh)
            {
                targetVelocity = highShotTargetVelocity;
            }
            
            if (!shotInProgress)
            {
                shooterMotorPIDController.setReference(targetVelocity, ControlType.kVelocity);
            }
            else
            {
                double motorVelocity = shooterMotorEncoder.getVelocity();
                double velocityTolerance = 5;
                if ( (motorVelocity > targetVelocity - velocityTolerance)  &&
                    (motorVelocity < targetVelocity + velocityTolerance) )
                {
                transferBeltMotorPower = 1.0;
                }
            }
        }
        else
        {
        if (shotInProgress)
        {
            shooterMotorPIDController.setReference(0, ControlType.kVelocity);
            shotInProgress = false;
        }
            intakeBeltMotor.set(ControlMode.PercentOutput, 0);
        }
        intakeArmControl.set(solenoidPosition);

        intakeRollerMotor.set(intakeRollerMotorPower);
        intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);
    }
}
