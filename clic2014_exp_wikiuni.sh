#!/bin/bash

echo wiki

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_semc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_semc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_msemc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_wiki_msemc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/wiki/it/termsvector.29.bin -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo ItWaC

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_semc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_semc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_msemc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_msemc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_400_3.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo ItWaC

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_semc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_semc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_msemc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_5_msemc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.pos_200_5.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo it semcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_semc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType prob_cross -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_semc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni

echo it multisemcor

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_msemc_p_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType prob -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_itwac_sv_msemc_o_wu -cm text -f xml -dsm /media/pierpaolo/data/ITWAC/ws/datastore.termvectors.bin -dsmType java -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c max -of task -depth 1 -sdType occ -wikiType uni
