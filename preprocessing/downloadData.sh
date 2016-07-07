#!/bin/bash

wget -A .gz -m -p -E -k -K -np http://www.wikibench.eu/wiki/2007-09/
wget -A .gz -m -p -E -k -K -np http://www.wikibench.eu/wiki/2007-10/
mv www.wikibench.eu/wiki/2007-09  /merged
mv www.wikibench.eu/wiki/2007-10  /merged
