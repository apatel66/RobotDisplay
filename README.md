# RobotDisplay
General purpose display for FIRST FRC robots (Uses ShuffleBoard)

!! Needs a 12V to 9V converter !!

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
- In any subsystem's `periodic()` method use the `public void add(String device, String key, String value)` method
  - Device: General part of the robot (or subsystem)
  - Key: Specific name of the value
  - Value: The values itself in a String object (use casting and the toString() method)
- Connect an ethernet cable from the Ethernet Shield to the radio
- Connect a 5V - 12V signal (preferably 9V) into the female power jack
- Upon deploying the robot code, buttons should appear on ShuffleBoard
- Use the buttons to control the current value being displayed


## Java
  - DisplaySubsystem
    - Contains an ArrayList of ArrayLists that contain all the values
    - Each internal ArrayList corresponds to a different part of the robot (such as a subsystem)    
    - The `add()` method handles the creation of new ArrayLists and updates values as well
    - Also handles the conversion of value pairs into JSON
    - Sends the JSON String to the Arduino
    - Sends a new value every 0.2 seconds
  - DisplayCommand
    - Each button on ShuffleBoard triggers this command with different coordinates
    - The command updates the coordinates in the DisplaySubsystem
    - The coordinates locate where the key and value is stored in the data array
  - JsonPair
    - The class that contains the key and values. This is the object that is encoded into JSON

## Arduino
Program for an Arduino (or Elegoo) Nano. Expects a JSON String in the form `{"key":"____", "value":"___"}`. The program then parses the string and prints the key on the first line and the value on the second. Make sure to update the IPs with the ones that pertain to your robot.

## Schematics and CAD
Contains a PDF file contianing drawings about how everything is wired along with measurements for the 3D printed parts. Also contains the current STL files for the Base, Lid, and Buckles. The buckle will likely need some sanding in order to fit snugly into the rest of the box.

## JAR Files
Required for functions such as JSON Conversion, UDP Communication, etc...

