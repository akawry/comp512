#!/bin/sh
cd bin
rmiregistry $1 &
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.FlightResourceManager $1 &
