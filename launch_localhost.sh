#!/bin/sh

if [ $# != 6 -a $# != 5 ] ; then
	echo "Usage : $0 rmi/tcp portcar portflight portroom portmiddleware automatic_client [input]"
	exit 1
fi	

( ./launch.sh $1 car $2 )
sleep 1
( ./launch.sh $1 flight $3 )
sleep 1
( ./launch.sh $1 room $4 )
sleep 1 
( ./launch.sh $1 middleware $5 -car=localhost:$2 -flight=localhost:$3 -room=localhost:$4 )
sleep 1
if [ $# = 6 ] ; then
	( ./launch_client.sh $1 localhost:$5 false:0:0:0 < $6 )
else 
	( ./launch_client.sh $1 localhost:$5 false:0:0:0  )
fi	

