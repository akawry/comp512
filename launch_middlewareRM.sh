#!/bin/sh
cd bin/
rmiregistry $1 &
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.RMI.RMIMiddleWare -port=$1 $2 $3 $4 &
