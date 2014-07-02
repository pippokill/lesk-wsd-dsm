#!/bin/bash

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_semc_pc -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_semc_pc -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_semc_pc -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c 0 -of task -depth 0 -sdType prob_cross

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_semc_pc -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_semc_pc -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross
