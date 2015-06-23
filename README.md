# DVESTA

Various Spring services and Spring components related to home automation based on Spring Boot.

## batterybackdata

A simple key-value database emulating the behaviour of battery backed up memory. 
Enables restarting services keeping counters and meters persistent.

Supports storing bits, floats, ints and shorts. Key is a string.

## chat

Enables the system sending chat messages over XMPP and Jabber.

## cl300

CL300 is a control computer for house heating from IVT. This module reads a serial ports with
 temperatures, state, timing etc.
 
## datalogger

Database for storing historical data in text files with a compact and simple format making it
 possible to derive the format of the files also in 100 years, if anyone is interested at that time.
 
## demo

A demo module, a template for new control modules.

## iodevices: adu208

Interfaces a ADU 208, an USB I/O module with relay output and digital inputs

## iodevices: k8055

Interfaces a Velleman K8055, an USB I/O module with TTL inputs, 500 mA digital outputs and analog in/out.

## iodevices: owfs

Interfaces a owfs filesystem, an interface to Dallas Semiconductor 1-wire bus. Assumes owfs is installed and
mounted on the controller host at /mnt/1wire.
 
## iodevices: Razberry

Interfaces a Raspberry Pi equipped with a Razberry which is a Z-Wave controller. HTTP REST interface. '

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
