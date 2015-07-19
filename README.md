# DVESTA

Various Spring services and Spring components related to home automation.

### Release notes
* Version 1.0.10 - 2015-07-19
  * Changed repo URL

## batterybackdata

A simple key-value database emulating the behaviour of battery backed up memory. 
Enables restarting services keeping counters and meters persistent.

Supports storing bits, floats, ints and shorts. Key is a string.

## chat

Send and receive chat messages over XMPP and Jabber.

## cl300

CL300 is a control computer for house heating from IVT. This module reads a serial ports with
 temperatures, state, timers and thresholds etc.
 
## datalogger

Database for storing historical data in text files with a compact and simple format making it
 possible to derive the format of the files also in 100 years, if anyone is interested at that time.
 
## demo

A demo module, a template for new control modules.

## iodevices: adu208

Interfaces a ADU 208, a USB I/O module with relay output and digital inputs

## iodevices: k8055

Interfaces a Velleman K8055, a USB I/O module with digital and analog inputs and outputs.

## iodevices: owfs

Interfaces Dallas Semiconductor 1-wire bus through a owfs filesystem. Assumes owfs is installed and
mounted on the controller host at /mnt/1wire.
 
## iodevices: Razberry

Interfaces a Raspberry Pi with a Razberry Z-Wave controller. HTTP REST interface.

## ioscan

An I/O interface layer enabling creation of controller software modules that is independent of specific 
I/O subsystem drivers/hardware.

## speech

A Spring text-to-speech service.

## system 

Useful stuff needed by production grade Spring Boot applications: REST endpoints for version and environment, build version,
 logging initializers per environment/spring-profile.
 
## tickengine

An execution framework supporting execution of control modules indifferent frequencies, inspired from 
real-time systems and PLCs.

## utils
 
Utility classes.
 
Simple standalone services:
- DaylightDarkness: Aggregates many DaylightSensorService instances.
- DoorBell: Control module reading a push-button and playing a sound
- PulseLight: Pulse flashing an analog output. Cool thing suggesting that your house computer is more advanced than it is
