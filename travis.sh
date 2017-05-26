#!/bin/bash

set -e

### Build output file setup

export OUTPUT=build.txt
touch $OUTPUT

tail_log() {
   echo 'The last 1000 lines of build output:'
   tail -1000 $OUTPUT
}

### Build error handler

on_error() {
  echo ERROR: An error was encountered with the build.
  tail_log
  exit 1
}
trap 'on_error' ERR

### Build heartbeat process (Travis kills processes with no output)

bash -c "while true; do echo Building Kapua...; sleep 30; done" &
HEARTBEAT_PROCESS_PID=$!

### Build itself

mvn -Dorg.eclipse.kapua.qa.waitMultiplier=5.0 -Pjavadoc >> $OUTPUT 2>&1
tail_log

### Cleaning up heartbeat process

kill $HEARTBEAT_PROCESS_PID