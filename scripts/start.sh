#!/bin/bash
java -cp "app-fat.jar;libs/*" AppKt "$@"
echo "Exit code: $?"