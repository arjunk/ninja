# Pre-requisites
----------------
1. JDK 1.5+
2. Android SDK 3.2(API Level 13)+


# Building and Running the app via Intellij
-----------------------------------------
### Pre-requisites
Intellij Idea with Android plugin enabled
 
##Steps 
1. Get the code
    git clone git://github.com/arjunk/ninja.git
2. Open the project in Intellij by selecting the TechRadar directory under the directory where the repository was cloned.This should have all the dependencies sorted out except for Android SDK
3. Configure the SDK by specifying Android SDK path at File--> Project Structure --> Platform Settings --> SDKs
4. To run on Emulator  
     1. Create a new Android Application Run Configuration with following properties
          Module : TechRadar
          Target Device : Emulator
          Preferred Virtual Device : AVD created for tablet. (If not already created, create a new one with SDK 3.0+)
     2. Start the AVD using the button near Preferred Virtual Device section in Run Configuration. Select "Scale Display to screen size". The emulator would start in landscape mode. Change it to Portrait mode (fn+control+F11) 
     3. Run the configuration.
5. To run on a tablet  
     1. Connect the device via USB cable.
     2. Enable USB Debugging mode on the device.
     3. Follow the steps mentioned at 4. Just select Target Device : USB Device.
     4. Run the configuration.

