package frc.robot.display;

public class JsonPair {
    public String key;
    public String value;

    public JsonPair(String k, String v) {
        this.key = k;
        this.value = v;
    }

    public void setKey(String k) {
        this.key = k;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
