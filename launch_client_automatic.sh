#!/bin/bash

function print_usage() {
	echo "Usage: $0 rmi/tcp host:port nbclient:nbloop:transac_sec:x"
	exit 1
}

if [ $# -ne 3 ] ; then
	print_usage ;
fi

cd bin/
case "$1" in 
	"tcp" )
		java Client.TCPClient $2 $3 ;;
	"rmi" )
		java -Djava.security.policy=client.policy Client.RMIClient $2 true:$3 ;;
	*)
		print_usage
esac
