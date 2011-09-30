#!/bin/sh

cd bin/
java ResImpl.CarResourceManager 2000  &
sleep 3
java ResImpl.FlightResourceManager 2001&
sleep 3
java ResImpl.RoomResourceManager 2002&
sleep 3
java ResImpl.CustomerResourceManager 2003 &
sleep 3
java ResImpl.MiddleWare -car=2000 -flight=2001  -room=2002 -customer=2003 -port=2004
#//killall java
