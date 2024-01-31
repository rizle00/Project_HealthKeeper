#include <SoftwareSerial.h>
#define USE_ARDUINO_INTERRUPTS true
#include <PulseSensorPlayground.h>
#include <Wire.h>
#include <Adafruit_MLX90614.h>


const int PulseWire = 0;
int Threshold = 550; 

SoftwareSerial BT(12, 13);  
const int buttonpin = 8;
int buttonstate = 0;
Adafruit_MLX90614 mlx = Adafruit_MLX90614();
PulseSensorPlayground pulseSensor;
// 시리얼모니터 열고
// AT 엔터 -> 응답 OK
// AT+NAMEXXXXXX -> 응답 OKsetname : 이름변경
// AT+PIN1234 -> 응답 OKsetPIN : 비밀번호변경
// AT+BAUD4 ->   응답 OK9600 : 통신속도변경 
//               1 -> 1200  2 -> 2400  3 -> 4800  4 -> 9600  5 ->19200  6 -> 38400  7 -> 57600  8 -> 115200
// AT+ROLE=S ->  응답 OK+ROLE:S   =>   M: Master S: Slave (기본 SLAVE 변경안해줘도 됨)

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  BT.begin(9600);
   pinMode(8,INPUT);
    pulseSensor.analogInput(PulseWire);  
  pulseSensor.setThreshold(Threshold);   
  pulseSensor.begin();
  mlx.begin();  



}

void loop() {
  int myBPM = pulseSensor.getBeatsPerMinute(); // 심박 측정
  float averageTemp = 0;
  if (pulseSensor.sawStartOfBeat()) { // 심박이 감지되면
    Serial.println("♥  A HeartBeat Happened ! "); // 심박 발생 메시지 출력
    Serial.print("BPM: "); // "BPM: " 출력
    Serial.println(myBPM); // 심박수 출력
     BT.print("BPM: ");
    BT.println(myBPM);
    // 온도 측정 및 출력
    
    for (int i = 0; i < 5; i++) {
      averageTemp += mlx.readObjectTempC(); // 온도 누적
      delay(20); // 온도 측정 사이의 지연
    }
    averageTemp /= 5; // 평균 온도 계산
    Serial.print("Average Temp: "); // "Average Temp: " 출력
    Serial.println(averageTemp); // 평균 온도 출력
     BT.print("Temp: "); 
    BT.println(averageTemp);
  }
  delay(1500);
 
  // if(BT.available()){
  //   Serial.write(BT.read());
  //   Serial.print(myBPM);
  // }
  // if(Serial.available()){
  //   BT.write(Serial.read());
  //   BT.print("BPM: ");
  //   BT.println(myBPM);
  //   BT.print("Temp: "); 
  //   BT.println(averageTemp);
  // }
   
  // BT.write(myBPM);
  //  if(buttonstate == HIGH)
  // {
  //   // button이 눌렸을때
  //   BT.println("L");
  //   delay(100);
  // }
  // if(buttonstate == LOW){
  //   // button이 눌리지 않았을
  //   BT.println("H");
  //   delay(100);
  // }
 
}
