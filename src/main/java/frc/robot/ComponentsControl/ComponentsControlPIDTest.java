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
        if (controlInputs.testShooter)
        {
            if (shotInProgress == false)
            {
                components.shooterMotorPIDController.setReference(6350, ControlType.kVelocity, 0);
                shotInProgress = true;
                firstShooterSpinupCompleted = false;
            }
            else
            {
                if (firstShooterSpinupCompleted == false)
                {
                    if (components.shooterMotorEncoder.getVelocity() > 6000)
                    {
                        SmartDashboard.putBoolean("DB/LED 0", true);
                        firstShooterSpinupCompleted = true;
                        components.shooterMotorPIDController.setReference(6350, ControlType.kVelocity, 2);
                    }
                }
                else
                {
                    
                }
            }
            
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
            components.transferBeltMotor.set(ControlMode.PercentOutput, 1.0);
        }
        else{ components.transferBeltMotor.set(ControlMode.PercentOutput, 0.0);}
    }
}