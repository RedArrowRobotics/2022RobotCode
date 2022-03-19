// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.cameraserver.CameraServer;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.TimedRobot;

import frc.robot.Autonomous.AutonomousAction;
import frc.robot.Autonomous.AutonomousActionDoNothing;
import frc.robot.Autonomous.AutonomousActionMoveAndCapture;
import frc.robot.Autonomous.AutonomousActionMoveBackward;
import frc.robot.Autonomous.AutonomousActionCaptureBall;
import frc.robot.Autonomous.AutonomousActionShootBallsFromCapturePoint;
//import frc.robot.Autonomous.AutonomousActionStraightMove;
import frc.robot.Autonomous.AutononmousActionShootAdaptiveFromCapturePoint;
import frc.robot.ComponentsControl.ComponentsControl;
import frc.robot.ComponentsControl.ComponentsControlV6;

import java.util.ArrayList;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private String m_autoSelected;
  
  private final DriveTrain driveTrain = new DriveTrain();
  private final ControlInputs controlInputs = new ControlInputs();
  private final SensorInputs sensorInputs = new SensorInputs();

  private ComponentsControl componentsControl;
  private Components components = new Components();
    
  private final String kAutoModeNull = "Do Nothing";
  private final String kAutoModeMoveBackward = "Move";
  private final String kAutoModeMoveBackAndGetBallThenShoot = "Move & Get Ball, Fixed Shot";
  private final String kAutoModeMoveBackwardAdaptiveShot = "Move, Ranged Shot";
  private final String kAutoModeCaptureBall = "Move, Get Ball";
  private final String kAutoModeCaptureBallAndShoot = "Move, Get Ball, Fixed Shot";
  private final String kAutoModeCaptureBallAndAdaptiveShoot = "Move, Get Ball, Ranged Shot";
  private final String kAutoModeCaptureSideBallAndShoot = "Wall Ball - Move, Get Ball, Fixed Shot";
  private ArrayList<AutonomousAction> autonomousSequence; 
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    components.intakeBeltMotor.setNeutralMode(NeutralMode.Brake);
    components.transferBeltMotor.setNeutralMode(NeutralMode.Brake);

    CameraServer.startAutomaticCapture();
    CameraServer.startAutomaticCapture();
    //driveFrontRight.setInverted(true);
    //driveFrontLeft.setInverted(true);
    componentsControl = new ComponentsControlV6();
    SmartDashboard.putStringArray("Auto List", 
      new String[]{kAutoModeNull, 
        kAutoModeMoveBackward,
        kAutoModeMoveBackAndGetBallThenShoot,
        kAutoModeMoveBackwardAdaptiveShot, 
        kAutoModeCaptureBall, 
        kAutoModeCaptureBallAndShoot,
        kAutoModeCaptureBallAndAdaptiveShoot,
        kAutoModeCaptureSideBallAndShoot});
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
    autonomousSequence = new ArrayList<AutonomousAction>();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kAutoModeNull);
    switch (m_autoSelected) {
      case kAutoModeMoveBackward:
        autonomousSequence.add(new AutonomousActionMoveBackward(84));
        break;
      case kAutoModeMoveBackAndGetBallThenShoot:
        autonomousSequence.add(new AutonomousActionMoveAndCapture(96));  
        autonomousSequence.add(new AutonomousActionShootBallsFromCapturePoint());
        break;
      case kAutoModeMoveBackwardAdaptiveShot:
        autonomousSequence.add(new AutonomousActionMoveBackward(84));
        autonomousSequence.add(new AutononmousActionShootAdaptiveFromCapturePoint());
        break;
      case kAutoModeCaptureBall:
        autonomousSequence.add(new AutonomousActionMoveBackward(84));
        autonomousSequence.add(new AutonomousActionCaptureBall());
        break;
      case kAutoModeCaptureBallAndShoot:
        autonomousSequence.add(new AutonomousActionMoveBackward(84));
        autonomousSequence.add(new AutonomousActionCaptureBall());
        autonomousSequence.add(new AutonomousActionShootBallsFromCapturePoint());
        break;
      case kAutoModeCaptureBallAndAdaptiveShoot:
        autonomousSequence.add(new AutonomousActionMoveBackward(84));
        autonomousSequence.add(new AutonomousActionCaptureBall());
        autonomousSequence.add(new AutononmousActionShootAdaptiveFromCapturePoint());
        break;
      case kAutoModeCaptureSideBallAndShoot:
        autonomousSequence.add(new AutonomousActionMoveBackward(60));
        autonomousSequence.add(new AutonomousActionCaptureBall());
        autonomousSequence.add(new AutonomousActionShootBallsFromCapturePoint());
      default:
        autonomousSequence.add(new AutonomousActionDoNothing());
        break;
    }
    autonomousSequence.get(0).Initialize(driveTrain, components, sensorInputs);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    if (autonomousSequence.size() > 0)
    {
      sensorInputs.readSensors();
      if (autonomousSequence.get(0).Execute(driveTrain, components, sensorInputs))
      {
        autonomousSequence.get(0).Finalize(driveTrain, components, sensorInputs);
        autonomousSequence.remove(0);
        if (autonomousSequence.size() > 0)
        {
          SmartDashboard.putBoolean("DB/LED 1", true);
          autonomousSequence.get(0).Initialize(driveTrain, components, sensorInputs);
        }
      }
    }
    else
    {
      driveTrain.arcadeDrive(0.0, 0.0);
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

    /*if (controlInputs.switchToBasicComponentControl)
    {
      componentsControl = new ComponentsControlV3();
    }*/

    double forward_power = 1.0;
    double turn_power = 1.0;

    driveTrain.arcadeDrive(
      controlInputs.driveStickY*forward_power,
      -controlInputs.driveStickX*turn_power);
  
    componentsControl.runComponents(components, controlInputs, sensorInputs);
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
