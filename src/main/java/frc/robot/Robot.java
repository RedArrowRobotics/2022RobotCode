// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.Autonomous.AutonomousAction;
import frc.robot.Autonomous.AutonomousActionDoNothing;
import frc.robot.ComponentsControl.ComponentsControl;
import frc.robot.ComponentsControl.ComponentsControlPIDTest;
import frc.robot.ComponentsControl.ComponentsControlV1;
import frc.robot.ComponentsControl.ComponentsControlV2;
import frc.robot.ComponentsControl.ComponentsControlV3;

import java.util.ArrayList;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private String m_autoSelected;
  
  private final CANSparkMax driveFrontLeft = new CANSparkMax(1, MotorType.kBrushless);
  private final CANSparkMax driveRearLeft = new CANSparkMax(2, MotorType.kBrushless);
  //private final MotorControllerGroup driveLeft = new MotorControllerGroup(driveFrontLeft, driveRearLeft);

  private final CANSparkMax driveFrontRight = new CANSparkMax(3, MotorType.kBrushless);
  private final CANSparkMax driveRearRight = new CANSparkMax(4, MotorType.kBrushless);
  //private final MotorControllerGroup driveRight = new MotorControllerGroup(driveFrontRight, driveRearRight);

  //private final DifferentialDrive robotDrive = new DifferentialDrive(driveLeft, driveRight);
  private final DifferentialDrive robotDrive = new DifferentialDrive(driveFrontLeft, driveFrontRight);

  private final ControlInputs controlInputs = new ControlInputs();
  private final SensorInputs sensorInputs = new SensorInputs();
  private final Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);

  private ComponentsControl componentsControl;
  private Components components = new Components();
    
  private final String kAutoModeNull = "Do Nothing";
  private final String kAutoModeMoveForward = "Move Forward";
  private final String kAutoModeCaptureBall = "Capture Ball";
  private ArrayList<AutonomousAction> automousSequence = new ArrayList<AutonomousAction>();
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    driveRearRight.follow(driveFrontRight, false);
    driveRearLeft.follow(driveFrontLeft, false);
    components.intakeBeltMotor.setNeutralMode(NeutralMode.Brake);
    components.transferBeltMotor.setNeutralMode(NeutralMode.Brake);

    CameraServer.startAutomaticCapture();
    CameraServer.startAutomaticCapture();
    //driveFrontRight.setInverted(true);
    //driveFrontLeft.setInverted(true);
    componentsControl = new ComponentsControlV3();
    SmartDashboard.putStringArray("Auto List", 
      new String[]{kAutoModeNull, kAutoModeMoveForward, kAutoModeCaptureBall});
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = SmartDashboard.getString("Auto Selector", kAutoModeNull);
    switch (m_autoSelected) {
      case kAutoModeMoveForward:
        break;
      case kAutoModeCaptureBall:
      default:
        automousSequence.add(new AutonomousActionDoNothing());
        break;
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (automousSequence.size() > 0)
    {
      if (automousSequence.get(0).Execute(components, sensorInputs))
      {
        automousSequence.remove(0);
      }
    }

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    controlInputs.readControls();
    sensorInputs.readSensors();

    if (controlInputs.switchToBasicComponentControl)
    {
      componentsControl = new ComponentsControlV3();
    }
    if (controlInputs.switchToSensorComponentControl)
    {
      componentsControl = new ComponentsControlPIDTest();
    }

    double forward_power = 1.0;
    double turn_power = 1.0;

    robotDrive.arcadeDrive(
      controlInputs.driveStickX*forward_power,
      -controlInputs.driveStickY*turn_power);
  
    componentsControl.runComponents(components, controlInputs, sensorInputs);

    if (controlInputs.runCompressor)
    {
      compressor.enableDigital();
    }
    else
    {
      compressor.disable();
    }

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    sensorInputs.readSensors();
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
