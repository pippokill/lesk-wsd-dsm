#!/bin/bash

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_semc_p_wu -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c 0 -of task -depth 0 -sdType prob_cross -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_semc_o_wu -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/en/ -sf bn -c 0 -of task -depth 0 -sdType occ -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_msemc_p_wu -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c 0 -of task -depth 0 -sdType prob -wikiType uni

./run.sh -i /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/data/multilingual-all-words.it.xml -o /home/pierpaolo/Scaricati/semeval-2013-task12-test-data/out/clic_msemc_o_wu -cm text -f xml -lang it -sc /home/pierpaolo/Develop/Java/lesk-wsd-dsm/resources/sense/it/ -sf bn -c 0 -of task -depth 0 -sdType occ -wikiType uni
