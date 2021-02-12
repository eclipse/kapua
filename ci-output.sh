#!/bin/bash

set -e

### Build output file setup

export OUTPUT=build.txt
touch $OUTPUT

tail_log() {
   echo "The last $1 lines of build output:"
   tail "$1" $OUTPUT
}

### Build error handler

on_error() {
  echo ERROR: An error was encountered with the build.
  tail_log -5000
  exit 1
}
trap 'on_error' ERR

### Build heartbeat process

bash -c "while true; do echo Building Kapua...; sleep 30; done" &
HEARTBEAT_PROCESS_PID=$!

### Run

"$@" >> $OUTPUT 2>&1
tail_log -1000

### Cleaning up heartbeat process

kill $HEARTBEAT_PROCESS_PID
