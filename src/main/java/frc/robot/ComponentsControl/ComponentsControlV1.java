package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;

public class ComponentsControlV1 extends ComponentsControl {

    @Override
    public void runComponents(ControlInputs controlInputs, SensorInputs sensorInputs) {
        
        Double intakeRollerMotorPower = controlInputs.runIntake ? -1.0 : 0.0;
        Double intakeBeltMotorPower = controlInputs.runIntake ? 1.0 : 0.0;
        Double transferBeltMotorPower = 0.0;

        if ( controlInputs.shootLow || controlInputs.shootHigh)
        {
            final double lowShotTargetVelocity = 3000;
            final double highShotTargetVelocity = 8000;
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
                shooterMotorPIDController.setReference(targetVelocity, ControlType.kVelocity, 0);
                shotInProgress = true;
                reallyShoot = false;
            }
            else
            {
                double motorVelocity = shooterMotorEncoder.getVelocity();
                SmartDashboard.putNumber("Shooter Motor Vel", motorVelocity);
                double velocityTolerance = 50;
                if (motorVelocity >= targetVelocity - velocityTolerance)
                {
                    reallyShoot = true;
                }
                /*if (motorVelocity >= targetVelocity)
                {
                    reallyShoot = true;
                }*/
                /*if ( (motorVelocity > targetVelocity - velocityTolerance)  &&
                    (motorVelocity < targetVelocity + velocityTolerance) )
                {
                    transferBeltMotorPower = 1.0;
                }*/
            }
        }
        else
        {
            if (shotInProgress)
            {
                shotInProgress = false;
                reallyShoot = false;
            }
            shooterMotor.set(0.0);
            //shooterMotorPIDController.setReference(0, ControlType.kVelocity, 0);                
        }

        transferBeltMotorPower = reallyShoot ? 1.0 : 0.0;

        intakeArmControl.set(controlInputs.deployIntake);

        intakeRollerMotor.set(intakeRollerMotorPower);
        intakeBeltMotor.set(ControlMode.PercentOutput, intakeBeltMotorPower);
        transferBeltMotor.set(ControlMode.PercentOutput, transferBeltMotorPower);
    }
}
