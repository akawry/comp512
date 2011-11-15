#!/bin/bash

function print_usage() {
	echo "Usage : $0 rmi/tcp portcar portflight portroom portmiddleware"
	exit 1
}

if [ $# != 5 ] ; then
  print_usage
fi	

( ./launch.sh $1 car $2 )
sleep 1
( ./launch.sh $1 flight $3 )
sleep 1
( ./launch.sh $1 room $4 )
sleep 1 
( ./launch.sh $1 middleware $5 -car=localhost:$2 -flight=localhost:$3 -room=localhost:$4 )
sleep 1
