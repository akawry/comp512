#!/bin/sh
chmod 711 bin/
chmod 644 bin/Client/*.class
cd bin
java -Djava.security.policy=client.policy Client.Client $1
