**minhash** - calculates and prints minhash for a given document or string.

lambdaprime <id.blackmesa@gmail.com>

# Download

You can download **minhash** from https://github.com/lambdaprime/minhash/tree/master/release

# Requirements

Java 11

# Usage

```
minhash [-s SEED] [-l LEN] [-ll] < -f DOCUMENT | STRING >
```

Where:

* DOCUMENT -- file for which minhash will be calculated
* STRING -- string for which minhash will be calculated

Options:

* -s SEED -- set seed value (default seed is taken randomly each time you run the application)
* -l LEN -- length of the minhash (default 128)
* -ll -- calculate minhash for each line of the document

# Examples

```
$
minhash -s 12345 -l 12 "Hello world"
23828323 732981638 901432142 531639452 335277093 1249369408 946483701 208918992 1187108515 754370683 433455118 415019326
$
minhash -s 12345 -l 12 -f README.md
48223938 24762595 13952067 6693296 126283087 44141764 3302847 90096366 9747865 91669439 9593949 78664975
```

