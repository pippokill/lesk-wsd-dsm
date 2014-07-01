#!/bin/sh
#---------------------------------#
# dynamically build the classpath #
#---------------------------------#
THE_CLASSPATH=./dist/lesk-wsd-dsm.jar
for i in `ls ./lib/*.jar`
do
  THE_CLASSPATH=${THE_CLASSPATH}:${i}
done

echo Run Lesk-wsd-dsm algorithm...

java -Xmx3g -cp ".:${THE_CLASSPATH}" di.uniba.it.exec.RunWSD $1 $2 $3 $4 $5 $6 $7 $8 $9 $10 $11 $12 $13 $14 $15 $16 $17 $18 $19 $20 $21 $22 $23 $24 $25 $26 $27 $28
