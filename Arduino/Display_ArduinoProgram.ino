// Demonstrates usage of the new udpServer feature.
// You can register the same function to multiple ports,
// and multiple functions to the same port.
//
// 2013-4-7 Brian Lee <cybexsoft@hotmail.com>
//
// License: GPLv2

#include <EtherCard.h>
#include <IPAddress.h>
#include <ArduinoJson.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>

StaticJsonDocument<512> jsonBuffer;
LiquidCrystal_I2C lcd(0x27, 16, 2);

const int buttonPin = 2;     // the number of the pushbutton pin
int buttonState = 0;   

#define STATIC 1  // set to 1 to disable DHCP (adjust myip/gwip values below)

#if STATIC
// Ethernet interface IP address
static byte myip[] = {10,27,67,203 };
// Gateway IP address
static byte gwip[] = { 10,27,67,1  };

static byte dnsip[] = { 10,27,67,1 };
#endif

// Ethernet MAC address - must be unique on your network
static byte mymac[] = { 0x70,0x69,0x69,0x2D,0x30,0x31 };

byte Ethernet::buffer[500]; // TCP/IP send and receive buffer

char* arr[3][4];
// Callback that prints received packets to the serial port
void udpSerialPrint(uint16_t dest_port, uint8_t src_ip[IP_LEN], uint16_t src_port, const char *data, uint16_t len){
  lcd.clear();
  IPAddress src(src_ip[0],src_ip[1],src_ip[2],src_ip[3]);

  Serial.print("dest_port: ");
  Serial.println(dest_port);
  Serial.print("src_port: ");
  Serial.println(src_port);


  Serial.print("src_ip: ");
  ether.printIp(src_ip);
  Serial.println("data: ");
  Serial.println(data);

  jsonBuffer.clear();

 // JsonObject root = jsonBuffer.parseObject(data);
  auto root = deserializeJson(jsonBuffer, data);
  if(root)
    Serial.println("Not Json");
   else
    Serial.println("Sys Good");


    /*for (int j = 0; j < root.value("arrSize"); ++j ) {
      for (int i = 0; i < 2; i+=2) {
        arr[j][i] = (const char*) root["sendArray"][j]["key"];
        arr[j][i+1] = (const char*) root["sendArray"][j]["value"];
     
         Serial.print(arr[j][i]);
         Serial.print("\t");
         Serial.println(arr[j][i+1]);
      }
    }*/
        //Serial.println(x); 

  lcd.setCursor(0,0);
  //lcd.print(arr[0][0]);
}

void setup(){

  lcd.begin();
  lcd.backlight();
  lcd.blink();
  Serial.begin(57600);
  Serial.println(F("\n[backSoon]"));

  // Change 'SS' to your Slave Select pin if you aren't using the default pin
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


  // Register udpSerialPrint() to port 42.
  ether.udpServerListenOnPort(&udpSerialPrint, 1234);
}

void loop(){
  // This must be called for ethercard functions to work.
  ether.packetLoop(ether.packetReceive());
}
