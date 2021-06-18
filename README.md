# FlightGearJoystick
The app we present using MVVM architecture, allows the user to control the flight in flightGear.

## Folders and Files
In the GIT folder of the FlightGearJoystick project there is the main branch with the lastest changes, a folder with PDFs explanations about the main classes. 
Our project is devided to 3 main folders:

#### Model
the code that responsible to send the data to flightGear server.

#### ViewModel
the code that responsible on passing the information from the user to the model.
By chanching the View the ViewModel is also changing (through data binding), and according to the change, different methods in the model are called. 

#### View
the code that responsible to the view the user see and use. In this case, the XML pages and the code behind it.


## Prerequests
In order to use our app, you will need to install it.

**Installing the app:**
clone our github, open the app with android studio and then you will be able to run it with the android studio emulator, or using android device.

**Changing the settings of flightGear**
In order to control a flight in flightgear on some device you will first need to change the settings of the flightgear on the device that have the flightgear you want to control:
1. go to *settings*
2. under the subtitle of Additional Settings change the text to: *--telnet=socket,in,10,127.0.0.1,6400,tcp*


## First running
After opening the app and changing the settings in flightgear, you now can control the flight!

**In flightgear:**
1. click the botton of ***Fly!***
2. you can change the view of the plain using *shift+V*
3. click the name of the plain, and then choose *Autostart*.

**In the app:**
1. enter port 6400
2. enter the ip of the device running the flightgear (you can use the ipconfig command in cmd)
3. click the botton *connect*

all is set, you are now controlling the flight with the sliders and the joystick.

***ENJOY THE FLIGHT***

## Link to UML of the project


## link to the video



