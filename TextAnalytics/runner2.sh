#!/bin/bash

prefix=`TZ='America/New_York' date +%F`

base_dir=/storage/vol0/ubuntu


java -Xmx4G -jar mainCommandLine.jar mainaddorreplacestockdata


dir="$base_dir"/ge/articles/010/
#ls $dir
nohup ./run.sh 2 $dir "$prefix"_ge_predictions.txt

dir="$base_dir"/mdlz/articles/011/
#ls $dir
nohup ./run.sh 4 $dir "$prefix"_mdlz_predictions.txt

dir="$base_dir"/cat/articles/010/
#ls $dir
nohup ./run.sh 6 $dir "$prefix"_cat_predictions.txt 

dir="$base_dir"/mcd/articles/010/
#ls $dir
nohup ./run.sh 7 $dir "$prefix"_mcd_predictions.txt 


