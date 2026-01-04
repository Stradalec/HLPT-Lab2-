#!/bin/bash

UNAME_OUT="$(uname -s)"
case "${UNAME_OUT}" in
    MINGW*|MSYS*|CYGWIN*)
        CP_SEP=';'
        ;;
    *)
        CP_SEP=':'
        ;;
esac

java -cp "app-fat.jar${CP_SEP}libs/*" AppKt "$@"
echo "Exit code: $?"