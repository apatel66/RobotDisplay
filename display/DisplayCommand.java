package frc.robot.display;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class DisplayCommand extends CommandBase {

    private DisplaySubsystem DISPLAY = RobotContainer.DISPLAY;

    private int yVal = 0;
    private int xVal = 0;

    public DisplayCommand (int y, int x) {
        addRequirements(DISPLAY);

        yVal = y;
        xVal = x;
    }

    //Change the y and x index that needs to be printed
    @Override
    public void initialize() {
        DISPLAY.displayY = yVal;
        DISPLAY.displayX = xVal;
    }

}