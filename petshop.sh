#!/bin/bash

echo "Running the Petshop..."
echo "JAVA_OPTS = ${JAVA_OPTS}"

mvn exec:java -Dexec.args="$@"
