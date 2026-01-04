#!/bin/bash

java -cp "libs/h2-2.3.232.jar" org.h2.tools.RunScript \
    -url "jdbc:h2:./data/coolDatabase;MODE=MySQL" \
    -user sa \
    -password "" \
    -script src/main/resources/db/migration/V1__init.sql

java -cp "libs/h2-2.3.232.jar" org.h2.tools.RunScript \
    -url "jdbc:h2:./data/coolDatabase;MODE=MySQL" \
    -user sa \
    -password "" \
    -script src/main/resources/db/migration/V2__addData.sql \


echo "База данных создана"