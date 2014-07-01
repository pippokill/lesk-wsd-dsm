#!/bin/bash

echo wiki

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_semc_p -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_semc_o -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_msemc_p -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_msemc_o -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ

echo dsm

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sf bn -c max -of task -depth 1

echo ItWaC

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_semc_p -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_semc_o -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_msemc_p -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_msemc_o -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ

echo dsm alone

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_pos_400_3 -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sf bn -c max -of task -depth 1

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_pos_200_5 -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sf bn -c max -of task -depth 1

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sf bn -c max -of task -depth 1

echo sense distribution alone

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_semc_p -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c 0 -of task -depth 0 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_semc_o -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c 0 -of task -depth 0 -sdType occ

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_msemc_p -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c 0 -of task -depth 0 -sdType prob

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_msemc_o -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c 0 -of task -depth 0 -sdType occ
