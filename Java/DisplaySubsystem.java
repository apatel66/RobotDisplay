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

public class DisplaySubsystem extends SubsystemBase {

    //Index of current value to display
    public static int displayX = 0;
    public static int displayY = 0;

    //Instantiate timer to control refresh rate
    public static Timer timer = new Timer();
    public static final double REFRESH_TIME = 0.2;

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

    //Adds/Updates new values to the main ArrayList
    public void add(String device, String key, String value) {

        int deviceIndex = -1;

        //Find the index of the selected device. If it doesn't exist, index = -1
        for (int i = 0; i < dataArray.size(); ++i) {
            if (dataArray.get(i).get(1).toLowerCase().equals(device.toLowerCase())) {
                deviceIndex = i;
                break;
            }
        }

        //If device doesn't exist, add a new row to the array and update the index
        if (deviceIndex == -1) {
            ArrayList<String> init = new ArrayList<String>();
            init.add("Name");
            init.add(device);
            dataArray.add(init);

            deviceIndex = dataArray.size() - 1;
        }

        //Check the row to see if the value already exists. If yes, update the value
        boolean newVal = true;
        for (int i = 0; i < dataArray.get(deviceIndex).size(); i += 2) {
            if (dataArray.get(deviceIndex).get(i).toLowerCase().equals(key.toLowerCase())) {
                dataArray.get(deviceIndex).set(i+1, value);
                newVal = false;
                break;
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

        //Convert the JSON string to bytes, then a DatagramPacket, then send over the UDP DatagramSocket
        buff = json.getBytes();
        DatagramPacket dp = new DatagramPacket(buff, buff.length, ip, socketNum);
        ds.send(dp);
    }

    //Every 0.2 seconds, display the value based on the current x and y value
    public void refreshDisplay() {
        if (timer.get() > REFRESH_TIME) {
            try {
                display(displayY, displayX);
            } catch (java.io.IOException e) {
                System.out.println("IOException");
            }

            timer.reset();
        }
    }

    //Continuously refresh the display
    public void periodic() {
        refreshDisplay();
    }
}
