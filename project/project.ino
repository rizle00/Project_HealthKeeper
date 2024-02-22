

#include <SoftwareSerial.h>
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <Adafruit_Sensor.h>
#include <SparkFun_ADXL345.h>
#include <Adafruit_ADXL345_U.h>

#include <PulseSensorPlayground.h> // Includes the PulseSensorPlayground Library.   
#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.

Adafruit_MLX90614 mlx = Adafruit_MLX90614();
Adafruit_ADXL345_Unified accel = Adafruit_ADXL345_Unified(12345);


PulseSensorPlayground pulseSensor;  // Creates an instance of the PulseSensorPlayground object called "pulseSensor"
#define BT_RXD 12
#define BT_TXD 11

SoftwareSerial BTSerial(BT_TXD,BT_RXD); // 블루투스 연결
const int PulseWire = A0;       // PulseSensor PURPLE WIRE connected to ANALOG PIN A2
// const int btn = 10;
float temp;
int x, y, z;
void setup() {
 Serial.begin(9600);        
BTSerial.begin(9600);     //블루투스 
 
// pinMode(btn, INPUT); // 버튼
mlx.begin();  //온도센서
pulseSensor.analogInput(PulseWire);   
pulseSensor.setThreshold(550);//심박 딜레이 기본 값
 if (pulseSensor.begin()) {
     Serial.println("We created a pulseSensor Object !");  //This prints one time at Arduino power-up,  or on Arduino reset.  
   }
   //가속도 시작
if (!accel.begin()) {
    Serial.println("Could not find a valid ADXL345 sensor, check wiring!");
    while (1);
  }
  accel.setRange(ADXL345_RANGE_2_G);
}

void loop() {
// 온도
  temp =0;
  for (int i = 0; i < 5; i++) {
    temp += mlx.readObjectTempC(); // 체온 c
    delay(100); // 임시 딜레이
  }
 temp /= 5;
  Serial.print("Average Temperature: ");
  Serial.print(temp,1);
  Serial.println("°C");
//심박
int myBPM = pulseSensor.getBeatsPerMinute(); // 심박
Serial.print("Heart Rate: ");
  Serial.println(myBPM);
//가속도
   sensors_event_t event;
  accel.getEvent(&event);
  int svm = 0;
  Serial.print("X: "); Serial.print(event.acceleration.x); Serial.print("  ");
  Serial.print("Y: "); Serial.print(event.acceleration.y); Serial.print("  ");
  Serial.print("Z: "); Serial.print(event.acceleration.z); Serial.println();
   Serial.print("SVM:");
   Serial.print(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z));
   if(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z)>=60){
    Serial.print(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z));
    svm = 1;
   } else{
    svm = 0;
   }
  Serial.println();

  delay(500); // 딜레이

  String dataString = "hr," + String(myBPM) + ",tp," + String(temp, 1) + ",ac," + String(svm);
  BTSerial.println(dataString);
if(BTSerial.available())//  블루투스에서 데이터 보냄
        Serial.write(BTSerial.read());

if(Serial.available())
        BTSerial.write(Serial.read());   

}

float calculateSVM(float x, float y, float z) {
  // SVM 계산
  return sqrt(x*x + y*y + z*z);
}




