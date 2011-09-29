#! /bin/sh

#javac -Xlint:unchecked -d bin/ $(find src/ -name '*.java');
javac -d bin/ $(find src/ -name '*.java');
