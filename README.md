An Enhanced Lesk Word Sense Disambiguation algorithm through a Distributional Semantic Model
==========================================================================

General info
------------

This software implements a Word Sense Disambiguation algorithm based on the simple Lesk approach integrating distributional semantics to compute the overlap between glosses.

Details about the algorithm are published in the following paper:

*Pierpaolo Basile, Annalina Caputo and Giovanni Semeraro*. **An Enhanced Lesk Word Sense Disambiguation algorithm through a Distributional Semantic Model**. Proceedings of COLING 2014, the 25th International Conference on Computational Linguistics: Technical Papers.

If you use this software in writing scientific papers, or you use this
software in any other medium serving scientists or students (e.g. web-sites,
CD-ROMs) please include the following citation:

@InProceedings{basile-caputo-semeraro:2014:Coling,
  author    = {Basile, Pierpaolo  and  Caputo, Annalina  and  Semeraro, Giovanni},
  title     = {An Enhanced Lesk Word Sense Disambiguation Algorithm through a Distributional Semantic Model},
  booktitle = {Proceedings of COLING 2014, the 25th International Conference on Computational Linguistics: Technical Papers},
  month     = {August},
  year      = {2014},
  address   = {Dublin, Ireland},
  publisher = {Dublin City University and Association for Computational Linguistics},
  pages     = {1591--1600},
  url       = {http://www.aclweb.org/anthology/C14-1151}
}

The adopted knowledge base is BabelNet ver. 1.1 available on-line: http://babelnet.org/download.jsp.

Usage
-----
In order to run lesk-wsd-dsm, you must execute the script run.sh in the main directory. 
The script requires the following parameters:

-i *input_file*: the file which contains the text that will be disambiguated

-o *output_file*: the file in which you want to save the output

-cm *sent|text|doc*: indicates how the input text will be read and disambiguated. The available values are: "sent" one sentence per time, "text" one text element per time, "doc" read the full file

-f *xml|oldxml|plain*: indicates the input file format. The available values are: "xml" for the SemEval-2013 Task 12 file format (http://www.cs.york.ac.uk/semeval-2013/task12/), "oldxml" old SemEval XML format, "plain" for plain text. The plain text must contain one token for each line. Each line must contains three values separated by TAB: token, pos-tag, lemma. The pos-tag has four possible values: n (noun), v (verb), j (adjective), r (adverb), any other character for other tags. An example is reported in the *text* folder

-dsm *file_path*: the binary file which describes the distributional semantic model (DSM). The model for the English language is available here https://dl.dropboxusercontent.com/u/66551436/termvectors_en.bin, while the model for the Italian language https://dl.dropboxusercontent.com/u/66551436/termvectors_it.java.bin (Italian language model is in Java format).

-dsmType *java|lucene*: set the DSM binary file format, java for DataOutputStream created by Java classes or Lucene format (luecene is the default value)

-lang *it|en|es|de|fr*: the language of the input text, *en* is the default value

-sc *dir_path*: the directory which contains information about both synsets probability and synsets occurrences. Files extracted from WordNet are in *resources/sense/en/*, while files extracted form MultiSemCor for Italian are in *resources/sense/it/*.

-sdType *prob|prob_cross|occ*: the function used to score synset. prob=conditional probability, occ=synset frequency, prob_cross=conditional probability computed on a different language

-wikiType *lev|uni*: the function used to score BabelSynset coming from Wikipedia. lev=Levenshtein similarity between lemmas, uni=uniform probability

-lc decimal:decimal: the weight assigned respectively to the DSM and the Sense Distribution. (default 0.5:0.5).

-sg *true|false*: enable/disable gloss scoring function (true is the default value)

-sf *wn|bn*: output synset format: "wn" uses WordNet offset, while "bn" uses BabelNet id (bn is the default value)

-c *integer value|max*: indicates the context size, "max" considers the whole text

-of *plain|task*: output format: "plain" uses plain text file to store word meanings, while task uses the file format defined in SemEval-2013 Task 12

-stem *true|false*: enable/disable glosses stemming

-depth *integer value*: the depth used to expand the synset gloss exploting related synsets

**EXAMPLE**
>This example performs Word Sense Disambiguation of the English dataset provided by the SemEval-2013 Task 12 organizers.
>./run.sh -i /home/user/semeval-2013-task12-test-data/data/multilingual-all-words.en.xml -o /home/user/semeval-2013-task12-test-data/out/lesk_wsd_dsm_test -cm doc -f xml -dsm ./resources/dsm/termvectors_en.bin -lang en -sc ./resources/sense/sense.freq -sf bn -c max -of task -depth 1

**RESOURCES**
Several resources are used by the algorithm:

>**BabelNet:** you must download and uncompress BabelNet ver. 1.1. Moreover you need to configure the following files: *babelnet.var.properties* and *jlt.var.properties*. Details are reported in the properties file. Moreover, you need to download the BabelNet Java API 1.1, uncompress it and copy the *resources* folder into the main algorithm folder.

>**Distribution Semantic Models (DSM):** a binary file which contains information about the vector space in which words are represented. It is possible to build a DSM from a text file. More information are reported in the class *di.uniba.it.wsd.dsm.Txt2Bin.java*.

>**Sense distribution:** a file which contains information about the probability assigned to each WordNet synset. A file for WordNet 3.0 synsets is provided. More information are reported in the class *di.uniba.it.wsd.tool.BuildFreqSense*.
