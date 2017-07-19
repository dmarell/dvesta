# DVESTA

Various Spring services and Spring components related to home automation.

### Release notes
* Version 2.0.4 - 2017-07-19
    * K8055: Suppress errors a few seconds in order to avoid to report short errors that immediately heals.
* Version 2.0.2 - 2017-07-13
    * Upgraded Spring Boot to 1.5.4
* Version 2.0.0 - 2016-04-04
    * Java 8 inspired API improvements for TickConsumer reducing tick execution initialization to a one-liner.
    * Introduced NamedTickConsumer and removed ExecutableModule
    * Removed DoorBell
* Version 1.3.2 - 2016-03-21
    * NPE bug fix in PUT/GET log-level.
* Version 1.3.1 - 2016-03-21
    * Added new endpoints PUT/GET log-level.
* Version 1.3.0 - 2016-03-02
    * Renamed resource /appversion to /version and /runenvironment to /environment
    * RunEnvironment: LOCAL, TEST, PROD. Removed AUTOSMALL and AUTOLARGE
    * Renamed remaining application config properties prefixed "bv83" to "dvesta"
    * Added SlackConnection
    * Razberry
      * Added support for door lock
      * Added optional Razberry HTTP Basic authentication using two new application config parameters:
        razberry.username
        razberry.password 
      * Added RazberryMonitor component for monitoring Razberry connection status
      * Added Slack monitor control module
      * Suppress events with same state as before
      * Avoid loosing events within the same second
      * Support inverted bit signals
    * K8055
      * Added K8055Monitor
      * Added Slack monitor control module
      * Fixed bug reading A1
      * Fixed bug reading inp5
      * Added NPE checks if user is configuring I/O outside valid ranges
* Version 1.2.0 - 2016-02-23
    * Added abstract method DeviceAddress.getGlobalDeviceIdentifier(), fix for problem related 
      to matching Razberry devices.
    * Upgraded Spring Boot version to 1.3.2.RELEASE.
* Version 1.1.1 - 2016-01-10
    * Added @Qualifier to TextChatConnection implementations
* Version 1.1.0 - 2016-01-07
    * Added Slack chat support
* Version 1.0.10 - 2015-07-19
    * Changed repo URL
      
## batterybackdata

A simple key-value database emulating the behaviour of battery backed up memory. 
Enables restarting services keeping counters and meters persistent.

Supports storing bits, floats, ints and shorts. Key is a string.

## chat

Send and receive text chat messages. Support for

* Jabber (XMPP)
* Slack

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

* DaylightDarkness: Aggregates many DaylightSensorService instances.
* DoorBell: Control module reading a push-button and playing a sound
* PulseLight: Pulse flashing an analog output, pretty cool.
