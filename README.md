An Enhanced Lesk Word Sense Disambiguation algorithm through a Distributional Semantic Model
==========================================================================

General info
------------

This software implements a Word Sense Disambiguation algorithm based on the simple Lesk approach integrating distributional semantics to compute the overlap between glosses.

Details about the algorithm will be published in the following paper:

*Pierpaolo Basile, Annalina Caputo and Giovanni Semeraro*. **An Enhanced Lesk Word Sense Disambiguation algorithm through a Distributional Semantic Model**. The paper was accepted to COLING 2014 conference.

If you use this software in writing scientific papers, or you use this
software in any other medium serving scientists or students (e.g. web-sites,
CD-ROMs) please include the following citation:

**(NOTE citation details are not yet available)**

The adopted knowledge base is BabelNet ver. 1.1 available on-line: http://babelnet.org/download.jsp. The software is compatible with only BabelNet 1.1.

Usage
-----
In order to run lesk-wsd-dsm, you must execute the script run.sh in the main directory. 
The script requires the following parameters:

-i *input_file*: the file which contains the text that will be disambiguated

-o *output_file*: the file in which you want to save the output

-cm *sent|text|doc*: indicates how the input text will be read and disambiguated. The available values are: "sent" one sentence per time, "text" one text element per time, "doc" read the full file

-f *xml|oldxml|plain*: indicates the input file format. The available values are: "xml" for the SemEval-2013 Task 12 file format (http://www.cs.york.ac.uk/semeval-2013/task12/), "oldxml" old SemEval XML format, "plain" for plain text. The plain text must contain one token for each line. Each line must contains three values separated by TAB: token, pos-tag, lemma. The pos-tag has four possible values: n (noun), v (verb), j (adjective), r (adverb), any other character for other tags. An example is reported in the *text* folder

-dsm *file_path*: the binary file which describes the distributional semantic model. A model for the English language is available here: https://dl.dropboxusercontent.com/u/66551436/termvectors_en.bin

-lang *it|en|es|de|fr*: the language of the input text, *en* is the default value

-sc *file_path*: the file which contains information about synsets distribution. A file extracted from WordNet 3.0 is provided in *resources/sense* folder

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


> Written with [StackEdit](https://stackedit.io/).