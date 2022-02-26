package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Components;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;

public class ComponentsControlPIDTest extends ComponentsControl {

    @Override
    public void runComponents(Components components, ControlInputs controlInputs, SensorInputs sensorInputs) 
    {
        final double targetVelocity = 5600;
        double currentVelocity = components.shooterMotorEncoder.getVelocity();
        if (controlInputs.testShooter)
        {
            /*if (shotInProgress == false)
            {
                components.shooterMotorPIDController.setReference(targetVelocity, ControlType.kVelocity, 1);
                shotInProgress = true;
                firstShooterSpinupCompleted = false;
            }
            else
            {
                if (firstShooterSpinupCompleted == false)
                {   
                    SmartDashboard.putBoolean("DB/LED 1", false);
                    if (currentVelocity > 5500)
                    {
                        SmartDashboard.putBoolean("DB/LED 0", true);
                        firstShooterSpinupCompleted = true;
                        components.shooterMotorPIDController.setReference(targetVelocity, ControlType.kVelocity, 3);
                    }
                }
                else
                {
                    final double velocityThreshold = 20.0;
                    if (currentVelocity >= targetVelocity - velocityThreshold && 
                         currentVelocity <= targetVelocity + velocityThreshold) {
                            SmartDashboard.putBoolean("DB/LED 1", true);
                            
                    } else {
                        SmartDashboard.putBoolean("DB/LED 1", false);
                    }
                }
            }*/
            components.shooterMotor.set(1.0);
            
        }
        else
        {
            shotInProgress = false;
            firstShooterSpinupCompleted = false;
            components.shooterMotor.set(0.0);
            SmartDashboard.putBoolean("DB/LED 0", false);                       
        }
        if (controlInputs.runTransferBelt)
        {
            components.transferBeltMotor.set(ControlMode.PercentOutput, 0.2);
        }
        else{ components.transferBeltMotor.set(ControlMode.PercentOutput, 0.0);}
    }
}