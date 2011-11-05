#! /bin/sh

#javac -Xlint:unchecked -d bin/ $(find src/ -name '*.java');
killall java
killall rmiregistry
javac -d bin/ $(find src/ -name '*.java');
