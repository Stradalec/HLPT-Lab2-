#!/bin/bash

java -cp "libs/h2-2.3.232.jar" org.h2.tools.RunScript \
    -url "jdbc:h2:./data/coolDatabase;MODE=MySQL" \
    -user sa \
    -password "" \
    -script ./scripts/init.sql

java -cp "libs/h2-2.3.232.jar" org.h2.tools.RunScript \
    -url "jdbc:h2:./data/coolDatabase;MODE=MySQL" \
    -user sa \
    -password "" \
    -script ./scripts/addData.sql \


echo "База данных создана"