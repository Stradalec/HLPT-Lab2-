#!/bin/bash

declare -a cases=(
  "--login alice --password qwerty --action read --resource A.B.C --volume 1"
  "--help|1"
  "|1"
  "--login alice --password wrongpass --action read --resource A.B.C --volume 1|2"
  "--login wronguser --password qwerty --action read --resource A.B.C --volume 1|3"
  "--login alice --password qwerty --action unknown --resource A.B.C --volume 1|4"
  "--login alice --password qwerty --action read --resource D --volume 1|5"
  "--login alice --password qwerty --action read --resource A.B.C.D --volume 1|6"
  "--login alice --password qwerty --action read --resource A.B.C --volume notanumber|7"
  "--login alice --password qwerty --action read --resource A.B.C --volume 20|8"
)

success_count=0
total=${#cases[@]}

for i in "${!cases[@]}"; do
  IFS='|' read -r args expected <<< "${cases[$i]}"
  java -cp "app.jar;libs/*" AppKt $args
  code=$?
  if [ $code -eq ${expected:-0} ]; then
    echo "Test $((i)): OK (exit $code)"
    ((success_count++))
  else
    echo "Test $((i)): FAIL (exit $code, expected ${expected:-0})"
  fi
done

echo "Passed $success_count out of $total tests"
