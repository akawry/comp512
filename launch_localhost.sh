#!/bin/sh
chmod -R 711 bin/
chmod -R 644 bin/ResImpl/*.class
chmod -R 644 bin/ResInterface/*.class

( ./launch_carRM.sh $1 )
sleep 1
( ./launch_flightRM.sh $2 )
sleep 1
( ./launch_roomRM.sh $3 )
sleep 1 
( ./launch_middlewareRM.sh $4 -car=localhost:$1 -flight=localhost:$2 -room=localhost:$3 )
sleep 1
( ./launch_client.sh localhost:$4 )
