

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

SoftwareSerial BTSerial(8,7);
const int PulseWire = A2;       // PulseSensor PURPLE WIRE connected to ANALOG PIN A2
const int btn = 10;

float temp;
int index = 0;
int x, y, z;
void setup() {
 Serial.begin(9600);        
BTSerial.begin(9600);     //블루투스 
 if (!accel.begin()) {
    Serial.println("Could not find a valid ADXL345 sensor, check wiring!");
    while (1);
  }
pinMode(btn, INPUT); // 버튼
mlx.begin();  //온도센서
pulseSensor.analogInput(PulseWire);   
pulseSensor.setThreshold(550);//심박 딜레이 기본 값
 if (pulseSensor.begin()) {
     Serial.println("We created a pulseSensor Object !");  //This prints one time at Arduino power-up,  or on Arduino reset.  
   }

}

void loop() {
   int myBPM = pulseSensor.getBeatsPerMinute(); // 심박
  temp += mlx.readObjectTempC(); // 체온 c
  index++;
  //0 1 2 3 4 (5)
  if(index >= 5){
    //결과출력
    temp = temp/5;
    Serial.println(temp);
    temp = 0;
    index =0;
  }
  // Serial.print("Ambient = "); Serial.print(mlx.readAmbientTempC()); 
  // Serial.print("*C\tObject = "); Serial.print(mlx.readObjectTempC()); Serial.println("*C");
//가속도
   sensors_event_t event;
  accel.getEvent(&event);

  Serial.print("X: "); Serial.print(event.acceleration.x); Serial.print("  ");
  Serial.print("Y: "); Serial.print(event.acceleration.y); Serial.print("  ");
  Serial.print("Z: "); Serial.print(event.acceleration.z); Serial.println();
   Serial.print("SVM:");
   if(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z)>=60){
    Serial.print(calculateSVM(event.acceleration.x, event.acceleration.y, event.acceleration.z));
  Serial.println();
   }
  

  delay(500);

  

if(BTSerial.available())//  블루투스에서 데이터 보냄
        Serial.write(BTSerial.read());

if(Serial.available())
        BTSerial.write(Serial.read());   
delay(500);
}

float calculateSVM(float x, float y, float z) {
  // SVM 계산
  return sqrt(x*x + y*y + z*z);
}
