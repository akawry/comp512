#!/bin/sh
echo $1
chmod 711 bin/
chmod 644 bin/ResImpl/*.class
chmod 644 bin/ResInterface/*.class
cd bin
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.CarResourceManager 2000 & 
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.FlightResourceManager 2001 &
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.RoomResourceManager 2002 & sleep 3
java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:`pwd`/ ResImpl.MiddleWare -car=localhost -flight=localhost -room=localhost -port=$1
#&
#java -Djava.security.policy=client.policy -Djava.rmi.server.codebase="file:$HOME/workspace/comp512/bin/ResImpl file:$HOME/workspace/comp512/bin/ResInterface" ResImpl.FlightResourceManager 2001 & 
#java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:$HOME/workspace/comp512/ ResImpl.RoomResourceManager 2002 &
#java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:$HOME/workspace/comp512/ ResImpl.CustomerResourceManager 2003 & sleep 5
#java -Djava.security.policy=client.policy -Djava.rmi.server.codebase=file:$HOME/workspace/comp512/ ResImpl.MiddleWare -car=localhost -flight=localhost -room=localhost -customer=localhost
