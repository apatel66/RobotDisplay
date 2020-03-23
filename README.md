# RobotDisplay
General purpose display for FIRST FRC robots (Uses ShuffleBoard)

## To Use
- Construct hardware with the given instructions
- Upload the Arduino code onto the Arduino Nano
  -  Change the My IP, Gateway IP, and DNS to match your network
- Add the Java folder to your code
  - Make sure to add the libraries (JAR files) to the Project Structure
  - Change the ipString variable (line 15) to match the "myip" specified in the Arduino program
- Initialize the Display in the RobotContainer class
```
public static DisplaySubsystem DISPLAY = new DisplaySubsystem();
```
- Declare the same Display in any subsystem you plan to use the display for
```
private static final DisplaySubsystem DISPLAY = RobotContainer.DISPLAY;
```
- In any subsystem's periodic method use the `public void add(String device, String key, String value)` method
  - Device: General part of the robot (or subsystem)
  - Key: Specific name of the value
  - Value: The values itself in a String object (use casting and the toString() method)
- Connect an ethernet cable from the Ethernet Shield to the radio
- Connect a 5V - 12V signal (preferably 9V) into the female power jack
- Upon deploying the robot code, buttons should appear on ShuffleBoard
- Use the buttons to control the current value being displayed


### Try My Programs Out!
If you would like to try my USACO programs out, here is how you can run them on a Command Line Interface:

1. Scroll up and click the green "Clone or download" button on the right, then click "Download ZIP". This will download a folder called "SampleCode-master" into your downloads folder.
2. Open up a new command line and enter the following to change your directory into the "SampleCode" folder.
```
$ cd Downloads/SampleCode-master
```
3. Enter `ls` to view the various program folders.
4. Enter `cd` + the folder name to go into the folder. The folders with executable programs will all start with "USACO".
   (Tip: If you have typed enough characters, press `Tab` and the rest of the word will fill in - given you are in the right 
   directory)
5. Enter `ls` to view the files inside the folder. There will be one program, a sample input, and a text file containing the 
   output. For eaxmple, if I was in the `USACOChecker` folder, I would see `checker.cpp`, `checker.in`, and 
   `checkerOutput.txt`.
6. To compile the program, enter the following code. Remember, even though you can enter any word in place of "example", 
   "problemName" has to be the exact file name of the program.
```
$ g++ -o example problemName.cpp
```
7. Now to run the program, enter:
```
$ ./example
```
8. An output should appear. To compare what was printed with the real answers, enter this command:
```
$ open problemNameOutput.txt
```
9. This will open a text file of the correct answer. Check to see if my program works!


## Java
I have solved many of the USACO Training problems in Java as well. However, I included a sample program I wrote as practice for my FIRST FRC robotics team. Its purpose is to control a robot drive base with a joystick, similar to a Xbox controller.
Click on the `JavaRobotCode` folder to see my code.


## Arduino
I also included a program I wrote as part of a reasearch project I am doing at Western Micigan University's Aersospace CubeSat Laboratory. The intent is to program an Arduino UNO micorcontroller to spin a reaction wheel.
Click on the `ArduinoResearchCode` folder to check it out.

