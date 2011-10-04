#!/bin/sh
echo $1
chmod -R 711 bin/
chmod -R 644 bin/ResImpl/*.class
chmod -R 644 bin/ResInterface/*.class

( ./launch_carRM.sh 2001 )
( ./launch_flightRM.sh 2002 )
( ./launch_roomRM.sh 2003 )
( ./launch_middlewareRM.sh 2004 -car=localhost -room=localhost -flight=localhost )
