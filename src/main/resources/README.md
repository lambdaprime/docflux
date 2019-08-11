**docflux** - set of tools to process text documents.

lambdaprime <id.blackmesa@gmail.com>

# Download

You can download **docflux** from https://github.com/lambdaprime/docflux/tree/master/release

# Requirements

Java 11

# Usage

```
minhash [-s SEED] [-l LEN] [-ll] [-dump] < -f DOCUMENT | STRING >
```

Calculates and prints minhash for a given document or string.

Where:

* DOCUMENT -- file for which minhash will be calculated
* STRING -- string for which minhash will be calculated

Options:

* -s SEED -- set seed value (default seed is taken randomly each time you run the application)
* -l LEN -- length of the minhash (default 128)
* -m MAX -- maximum values in minhash (needs to be prime, default 2147483647)
* -ll -- calculate minhash for each line of the document
* -dump -- shows minhash details

```
distance [ --minhash OPTIONS_STRING ] < SET1 > < SET2 >
```

Calculates Jaccard similarity distance between two sets. As sets you can use previously calculated minhashes.

Where:

* SETN -- set of numbers of words
* OPTIONS_STRING -- string of options which will be passed to minhash command or empty string "" if none (see examples). In case of empty string the default seed will be used for each minhash.

Options:

* --minhash -- turns on minhash mode when distance is calculated not between the input SETs but between their minhashes

# Examples

```
$
minhash -s 12345 -l 12 "There is no spoon"
844891981 475746331 1024964782 470279604 592399321 341907980 544233204 271350935 236247775 414750817 25601593 1304639739
$
minhash -s 12345 -l 12 "There is no minhash"
844891981 179079962 1024964782 470279604 592399321 287362302 1277087821 162046433 236247775 414750817 25601593 1304639739
$
minhash -s 12345 -l 12 -f README.md
48223938 24762595 13952067 6693296 126283087 44141764 3302847 90096366 9747865 91669439 9593949 78664975
$
distance "844891981 475746331 1024964782 470279604 592399321 341907980 544233204 271350935 236247775 414750817 25601593 1304639739" "844891981 179079962 1024964782 470279604 592399321 287362302 1277087821 162046433 236247775 414750817 25601593 1304639739"
0.5
$
distance --minhash "" "Good morning" "Good evening"
0.19069767441860466
$
distance --minhash "-s 123" "Good morning" "Good evening"
0.24271844660194175
```
