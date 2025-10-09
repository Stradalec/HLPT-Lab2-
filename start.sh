#!/bin/bash
java -cp "app.jar;libs/*" AppKt "$@"
echo "Exit code: $?"