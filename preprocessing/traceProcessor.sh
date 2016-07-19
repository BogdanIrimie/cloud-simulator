#!/bin/bash

FILES=merged/*
for file in $FILES
do
  echo "Processing $file file..."
  # Uncompress file, extract only the first two columns and sort after time column.
  awk '{print $1 " " $2}' <(gunzip -c $file) | sort -nk 2 -o $(echo $file | sed 's/.gz//g')"_sorted"
  rm $file
done

# Merge sort all files using time field and split the output.
echo "Start merge sort!"
sort -m -nk 2 --batch-size=1021 merged/*_sorted | split -d -l 8000000 - merged/trace_
echo "Finish merge sort!"

# Remove intermediary files.
rm *_sorted
