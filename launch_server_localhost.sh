#!/bin/bash

function print_usage() {
	echo "Usage : $0 rmi/tcp portcar1 portcart2 portflight1 portflight2 portroom1 portroom2 portmiddleware"
	exit 1
}

if [ $# != 8 ] ; then
  print_usage
fi	

( ./launch.sh $1 car $2 )
( ./launch.sh $1 car $3 )
sleep 1

( ./launch.sh $1 flight $4 )
( ./launch.sh $1 flight $5 )
sleep 1

( ./launch.sh $1 room $6 )
( ./launch.sh $1 room $7 )
sleep 1 

( ./launch.sh $1 middleware $8 -car=localhost:$2,localhost:$3 -flight=localhost:$4,localhost:$5 -room=localhost:$6,localhost:$7 )
sleep 1
