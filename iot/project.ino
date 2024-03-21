



#include <SoftwareSerial.h>
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <Adafruit_Sensor.h>

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


void setup() {
 Serial.begin(9600);        
BTSerial.begin(9600);     //블루투스 
 
 
mlx.begin();  //온도센서
pulseSensor.analogInput(PulseWire);   
pulseSensor.setThreshold(550);//심박 딜레이 기본 값
 if (!pulseSensor.begin()) {
 Serial.println("심박센서 없음");
    while (1);
}

 if (!accel.begin()) {
    Serial.println("가속도 센서 없음");
    while (1);
  }
}

void loop() {
 
 
// 온도
 float temp =0;
  for (int i = 0; i < 5; i++) {
    temp += mlx.readObjectTempC(); // 체온 c
  
    delay(1000); // 임시 딜레이
  }
 temp /= 5;
 int heartRate = pulseSensor.getBeatsPerMinute();
   // 사고 여부 판단
    sensors_event_t event;
    accel.getEvent(&event);
    float svm = calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z);
    int accident = svm >= 60? 1:0; // 예시에서는 SVM이 60 이상이면 사고로 판단
    

  String dataString = "hr," + String(heartRate) + ",tp," + String(temp, 1) + ",ac," + String(svm);
  // hr,65,tp,36.3,ac,1
  BTSerial.println(dataString);
    Serial.print("Temperature: "); Serial.println(temp);
    Serial.print("Heart Rate: "); Serial.println(heartRate);
    Serial.print("Accident: "); Serial.println(accident);
      Serial.print("SVM:"); Serial.println(svm);
  // delay(5*1000); 
if(BTSerial.available())//  블루투스에서 데이터 보냄
        Serial.write(BTSerial.read());

if(Serial.available())
        BTSerial.write(Serial.read());   

}

float calculateSVM(float x, float y, float z) {
  // SVM 계산
  return sqrt(x*x + y*y + z*z);
}

