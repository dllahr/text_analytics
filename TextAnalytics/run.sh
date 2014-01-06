#!/bin/bash

index=$1
input_dir=$2
output=$3

/usr/local/bin/java/java -Xmx17G -jar mainCommandLine.jar mainarticlestopredictions $index "$input_dir" &> "$output"
cat "$output" | mail -s "from aws" dllahr@gmail.com

