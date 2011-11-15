#!/bin/sh

function print_usage() {
	echo "Usage : $0 rmi/tcp portcar portflight portroom portmiddleware manual/automatic [loop:trsec:type]"
	exit 1
}

if [ $# != 7 -a $# != 6 ] ; then
  print_usage
fi	

( ./launch_server_localhost.sh $1 $2 $3 $4 $5)

case "$6" in
  "manual" )
	( ./launch_client_manual.sh $1 localhost:$5 ) ;;
  "automatic" )
	( ./launch_client_automatic.sh $1 localhost:$5 $7 ) ;;
  *)
    print_usage
  esac

