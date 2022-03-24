package frc.robot;

//import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleBoardSetup {
    //ShuffleboardTab tab = Shuffleboard.getTab("WorkAround");

    public final void AutoTab( SendableChooser<String> chooser ){
        /*tab
            .add("Chosen_Auto", chooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withSize(4*6, 4*4);
        Shuffleboard.selectTab("WorkAround");*/
        SmartDashboard.putData(chooser);
        Shuffleboard.selectTab(3);
    }
    public final void TeleTab(){
        Shuffleboard.selectTab(4);
    }
}
