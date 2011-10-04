#!/bin/sh
echo $1
chmod -R 711 bin/
chmod -R 644 bin/ResImpl/*.class
chmod -R 644 bin/ResInterface/*.class
cd bin
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.CarResourceManager 2000 & 
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.FlightResourceManager 2001 &
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.RoomResourceManager 2002 & sleep 3
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.MiddleWare -car=localhost -flight=localhost -room=localhost -port=$1
