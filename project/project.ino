

#include <SoftwareSerial.h>
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_ADXL345_U.h>

#include <PulseSensorPlayground.h> // Includes the PulseSensorPlayground Library.   
#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.

Adafruit_MLX90614 mlx = Adafruit_MLX90614();
Adafruit_ADXL345_Unified accel = Adafruit_ADXL345_Unified(0x53);

PulseSensorPlayground pulseSensor;  // Creates an instance of the PulseSensorPlayground object called "pulseSensor"

SoftwareSerial BTSerial(12,11);
const int PulseWire = A0;       // PulseSensor PURPLE WIRE connected to ANALOG PIN A2

int x, y, z;

unsigned long previousMillis = 0;
const long interval = 5000; // 5초마다 보낼 것

void setup() {
 Serial.begin(9600);        
BTSerial.begin(9600);     //블루투스 
 if (!accel.begin()) {
    Serial.println("Could not find a valid ADXL345 sensor, check wiring!");
    while (1);
  }

mlx.begin();  //온도센서
pulseSensor.analogInput(PulseWire);   
pulseSensor.setThreshold(550);//심박 딜레이 기본 값
 if (pulseSensor.begin()) {
     Serial.println("We created a pulseSensor Object !");  //This prints one time at Arduino power-up,  or on Arduino reset.  
   }

}

void loop() {
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;

     // 체온 측정
    float temperature = mlx.readObjectTempC();

    // 심박 측정
    int heartRate = pulseSensor.getBeatsPerMinute();

    // 사고 여부 판단
    sensors_event_t event;
    accel.getEvent(&event);
    float svm = calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z);
    bool accident = svm >= 60; // 예시에서는 SVM이 60 이상이면 사고로 판단

    BTSerial.print(temperature);
    BTSerial.print(",");
    BTSerial.print(heartRate);
    BTSerial.print(",");
    BTSerial.println(accident ? "1" : "0");

    Serial.print("Temperature: "); Serial.println(temperature);
    Serial.print("Heart Rate: "); Serial.println(heartRate);
    Serial.print("Accident: "); Serial.println(accident ? "Yes" : "No");
  Serial.print("X: "); Serial.print(event.acceleration.x); Serial.print("  ");
  Serial.print("Y: "); Serial.print(event.acceleration.y); Serial.print("  ");
  Serial.print("Z: "); Serial.print(event.acceleration.z); Serial.println();
   Serial.print("SVM:");
   if(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z)>=60){
    Serial.print(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z));
  Serial.println();
   }
  
}
if(BTSerial.available())//  블루투스에서 데이터 보냄
        Serial.write(BTSerial.read());

if(Serial.available())
        BTSerial.write(Serial.read());   

}

float calculateSVM(float x, float y, float z) {
  // SVM 계산
  return sqrt(x*x + y*y + z*z);
}
