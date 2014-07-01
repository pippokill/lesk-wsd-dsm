#!/bin/bash


echo ItWaC

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_semc_p -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_semc_o -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_msemc_p -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_msemc_o -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_semc_p -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_semc_o -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_msemc_p -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_msemc_o -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ


