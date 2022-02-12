package frc.robot.ComponentsControl;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.ControlInputs;
import frc.robot.SensorInputs;


public abstract class ComponentsControl {

    protected final CANSparkMax intakeRollerMotor = new CANSparkMax(6, MotorType.kBrushless);
    protected final TalonSRX intakeBeltMotor = new TalonSRX(7);
    protected final TalonSRX transferBeltMotor = new TalonSRX(8);

    protected final CANSparkMax shooterMotor = new CANSparkMax(5, MotorType.kBrushless);
    protected final RelativeEncoder shooterMotorEncoder = shooterMotor.getEncoder();
    protected final SparkMaxPIDController shooterMotorPIDController = shooterMotor.getPIDController();

    protected final Solenoid intakeArmControl = new Solenoid(PneumaticsModuleType.CTREPCM, 0); 

    protected boolean shotInProgress = false;
    protected boolean reallyShoot = false;
    protected boolean waitForSpinUp = false;
    protected Integer shooterThresholdCount = 0;
    public abstract void runComponents(ControlInputs controlInputs, SensorInputs sensorInputs);
}
