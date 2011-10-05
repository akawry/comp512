#!/bin/bash

cd bin/
java_run="java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/"

if [ $1 == "tcp" ] ; then
	case "$2" in
		"room")
			$java_run ResImpl.TCP.RoomTCPResourceManager $3 &  ;;
		"car")
			$java_run ResImpl.TCP.CarTCPResourceManager $3 &  ;;
		"flight")
			$java_run ResImpl.TCP.FlightTCPResourceManager $3 &  ;;
		"middleware")
			if [ $# != 6 ] ; then
				echo "Usage : $0 tcp middleware port -car=host:port -room=host:port -car=host:port"
				exit 1
			fi
			$java_run ResImpl.TCP.TCPMiddleWareServer -port=$3 $4 $5 $6  &  ;;
		*)
		echo "Usage : $0 tcp room/car/flight/middleware [......]"
		exit 1
	esac

elif [ $1 == "rmi" ] ; then 
	rmiregistry $3 &
	case "$2" in
		"room")
			$java_run ResImpl.RMI.RoomRMIResourceManager $3 &  ;;
		"car")
			$java_run ResImpl.RMI.CarRMIResourceManager $3 &  ;;
		"flight")
			$java_run ResImpl.RMI.FlightRMIResourceManager $3 &  ;;
		"middleware")
			if [ $# != 6 ] ; then
				echo "Usage : $0 rmi middleware port -car=host:port -room=host:port -car=host:port"
				exit 1
			fi
			$java_run ResImpl.RMI.RMIMiddleWare -port=$3 $4 $5 $6  &  ;;
		*)
		echo "Usage : $0 rmi room/car/flight/middleware [......]"
		exit 1
	esac
else 
	echo "First argument should be either tcp or rmi"
	exit 1
fi
