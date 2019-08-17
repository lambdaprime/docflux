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

```
find-similar-line [-overlap] [-jaccard] LINE FILE
```

Finds lines from FILE with highest similarity to the query LINE.

Options:

* --overlap -- overlap coefficient similarity measure
* --jaccard -- Jaccard similarity measure

```
find-similar-file FILE DIR
```

Finds files from DIR with m
highest Jaccard similarity to the query FILE.

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
$
find-similar-line -overlap "Terra victory day" "datasets/The Variable Man.txt"
[1.0] “We have lost the war,” Margaret Duffe stated quietly. “But this is not a day of defeat. It is a day of victory. The most incredible victory Terra has ever had.”
[0.6666666666666666] At eight o’clock in the evening of May 15, 2136, Icarus was launched toward the star Centaurus. A day later, while all Terra waited, Icarus entered the star, traveling at thousands of times the speed of light.
[0.3333333333333333] “It will end,” Reinhart stated coldly, “as soon as Terra turns out a weapon for which Centaurus can build no defense.”
[0.3333333333333333] 21-17 on the Centauran side. But a month ago it had been 24-18 in the enemy’s favor. Things were improving, slowly but steadily. Centaurus, older and less virile than Terra, was unable to match Terra’s rate of technocratic advance. Terra was pulling ahead.
[0.3333333333333333] And then the long, dreary years of inaction between enemies where contact required years of travel, even at nearly the speed of light. The two systems were evenly matched. Screen against screen. Warship against power station. The Centauran Empire surrounded Terra, an iron ring that couldn’t be broken, rusty and corroded as it was. Radical new weapons had to be conceived, if Terra was to break out.
```
