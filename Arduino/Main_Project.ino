//
#include <EtherCard.h>
#include <IPAddress.h>
#include <ArduinoJson.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>

//Buffer & LCD
StaticJsonBuffer<512> jsonBuffer;
LiquidCrystal_I2C lcd(0x27, 16, 2);

//1 Disables DHCP
#define STATIC 1 

// Ethernet interface IP address, Gateway IP, DNS IP and MAC address
#if STATIC
static byte myip[] = {10,27,67,203 };
static byte gwip[] = { 10,27,67,1  };
static byte dnsip[] = { 10,27,67,1 };
#endif
static byte mymac[] = { 0x70,0x69,0x69,0x2D,0x30,0x31 };

// UDP send and receive buffer
byte Ethernet::buffer[500]; 

void udpSerialPrint(uint16_t dest_port, uint8_t src_ip[IP_LEN], uint16_t src_port, const char *data, uint16_t len) {
  lcd.clear();
  IPAddress src(src_ip[0],src_ip[1],src_ip[2],src_ip[3]);

  //Ouput the data onto the Serial Monitor for debugging
  Serial.println("data: ");
  Serial.println(data);
  jsonBuffer.clear();

  //Figures out if the packet recieved is valid json.
  JsonObject& root = jsonBuffer.parseObject(data);
  if(root.success())
    Serial.println("Sys Good");
   else
    Serial.println("not json");
    lcd.clear();

    //Prints data to LCD screen 
    lcd.setCursor(0, 0);
    lcd.print((const char*) root["key"]);    
    lcd.setCursor(0,1);
    lcd.print((const char*) root["value"]);
    Serial.println("\n"); 
    lcd.setCursor(0,0);
}

void setup()
{
    Serial.begin(9600);
    
  //Init LCD
  lcd.begin();
  lcd.backlight();
  lcd.blink();
  
  //Init Ethernet drivers
  if (ether.begin(sizeof Ethernet::buffer, mymac, SS) == 0)
      Serial.println(F("Failed to access Ethernet controller"));
  #if STATIC
    ether.staticSetup(myip, gwip, dnsip);
  #else
    if (!ether.dhcpSetup())
      Serial.println(F("DHCP failed"));
  #endif
  ether.printIp("IP:  ", ether.myip);
  ether.printIp("GW:  ", ether.gwip);
  ether.printIp("DNS: ", ether.dnsip);

  //Begin listening
  ether.udpServerListenOnPort(&udpSerialPrint, 1234);
  
}

//Loops to wait for incoming packets
void loop()
{
  ether.packetLoop(ether.packetReceive());
}
