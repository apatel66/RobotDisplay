package frc.robot.display;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/*
To Use the Arduino Nano Display...
- Instantiate a DisplaySubsystem in the RobotContainer class normally.
- Instantiate a DisplaySubsystem in the Robot class and any subsystem and set it equal to the RobotContainer DisplaySubsystem.
- In the RobotInit method or subsystem constructor, call the addDevice method to set up what subsystems and what other parts of the robot are going to be montiored
- In the teleopPeriodic method, use the refreshDisplay method to constantly update the display to the current index
- Set up public static int y and x in the Robot class.
- Either in the teleopPeriodic or a subsystem periodic method, use the addToArray method to add values/ShuffleBoard commands
- Use casting and the .toString() method to get the string value of an object

- addDevice (String deviceName)
- addToArray (String device, String key, String value)
- refreshDisplay (int x, int x)
*/



public class DisplaySubsystem extends SubsystemBase {

    //Index of current value to display
    public static int displayX = 0;
    public static int displayY = 0;

    //Instantiate timer to control refresh rate
    public static Timer timer = new Timer();
    public static double refreshTime = 0.2;

    //UDP variables and objects
    public static DatagramSocket ds;
    public static InetAddress ip;
    public static byte[] buff = null;
    public static String sendString = "";
    public static int socketNum = 1234;

    //JSON Adapters
    public static Moshi moshi = new Moshi.Builder().build();
    public static JsonAdapter<JsonPair> jsonAdapter = moshi.adapter(JsonPair.class);

    //Array holding all the values
    public static ArrayList<ArrayList<String>> dataArray = new ArrayList<ArrayList<String>>();

    public DisplaySubsystem () throws IOException {
        timer.start();
        ds = new DatagramSocket();
        ip = InetAddress.getByName("10.27.67.203");

        //Set initial message
        ArrayList<String> init = new ArrayList <String>();
        init.add("StrykeForce");
        init.add("InfiniteRecharge");
        dataArray.add(init);
    }

    //Creates a new ArrayList for a different part of the robot and adds it to the main ArrayList
    public void addDevice(String deviceName) {
        boolean deviceDoesNotExist = true;

        //If program tries to create two of the same devices, an error will be thrown
        for (int i = 0; i < dataArray.size(); ++i) {
            if (dataArray.get(i).get(1).equals(deviceName)) {
                deviceDoesNotExist = false;
                throw new RuntimeException("Device: \"" + deviceName + "\" already exists");
            }
        }

        //If the device is new, add an ArrayList
        if(deviceDoesNotExist) {
            ArrayList<String> init = new ArrayList<String>();
            init.add("Name");
            init.add(deviceName);
            dataArray.add(init);
        }
    }

    //Adds/Updates new values to the main ArrayList
    public void addToArray(String device, String key, String value) {
        boolean newVal = false;
        boolean valueExists = false;

        int deviceIndex = 0;
        for (int i = 0; i < dataArray.size(); ++i) {
            //If the value is new or is updated, then break
            if (newVal || valueExists) {
                break;
            }

            if (dataArray.get(i).get(1).equals(device)) {
                for (int j = 0; j < dataArray.get(i).size(); j+=2) {
                    //Check to see if value already exists
                    if (dataArray.get(i).get(j).equals(key)) {
                        dataArray.get(i).set(j+1, value);
                        valueExists = true;
                        break;
                    }

                    //If the end of the ArrayList is reached, then the added value must be new
                    else if (j == dataArray.get(i).size() - 2) {
                        newVal = true;
                        deviceIndex = i;
                        break;
                    }
                }
            }

            //If the user tries finding a nonexistent device, an error will be thrown
            else if (i == dataArray.size() - 1) {
                throw new RuntimeException("Device: \"" + device + "\" not found");
            }
        }

        //If the value added is new, add it to the array and create a button for it on ShuffleBoard
        if (newVal) {
            dataArray.get(deviceIndex).add(key);
            dataArray.get(deviceIndex).add(value);
            SmartDashboard.putData(device+"\n"+key, new DisplayCommand(deviceIndex, dataArray.get(deviceIndex).size()-2));
        }
    }

    //Create a new JSON pair, containing a key and value, then convert it to a JSON string
    public void display(int y, int x) throws IOException {
        JsonPair sendPair =  new JsonPair(dataArray.get(y).get(x), dataArray.get(y).get(x+1));
        String json = jsonAdapter.toJson(sendPair);
        sendJson(json);
    }

    //Convert the JSON string to bytes, then a DatagramPacket, then send over the UDP DatagramSocket
    public void sendJson(String toSend) throws IOException {
        buff = toSend.getBytes();
        DatagramPacket dp = new DatagramPacket(buff, buff.length, ip, socketNum);
        ds.send(dp);
    }

    //Every 0.2 seconds, display the value based on the current x and y value
    public void refreshDisplay() {
        if (timer.get() > refreshTime) {
            try {
                display(displayY, displayX);
            } catch (java.io.IOException e) {
                System.out.println("IOException");
            }
            timer.reset();
        }
    }

    public void periodic() {
        refreshDisplay();
    }

}
