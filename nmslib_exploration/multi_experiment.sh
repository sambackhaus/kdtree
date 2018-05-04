#!/bin/bash

path_to_experiment_script=$1 # this path should point to the script in nmslib: <...>/nmslib/similarity_search/release/experiment
path_to_data=$2
path_to_queries=$3

output_prefix="out_"

declare -a arr=("vptree" "mvptree" "ghtree" "list_clusters" "satree" "bbtree" "lsh_multiprobe" "lsh_gaussian" "lsh_cauchy" "lsh_threshold" "proj_incsort" "proj_vptree" "omedrank" "pp-index" "mi-file" "napp" "perm_incsort_bin" "perm_bin_vptree" "sw-graph" "hnsw" "nndes" "seq_search")

echo "MethodName	Recall	Recall@1	PrecisionOfApprox	RelPosError	NumCloser	ClassAccuracy	QueryTime	DistComp	ImprEfficiency	ImprDistComp	Mem	IndexTime	IndexLoadTime	IndexSaveTime	QueryPerSec	IndexParams	QueryTimeParams	NumData" > result.dat
for i in "${arr[@]}"
do
   echo "start $i xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
   $path_to_experiment_script -i $path_to_data -s l1 -q $path_to_queries -k 150 -m $i -o $output_prefix$i
   tail -n1 $output_prefix$i"_K=150.dat" >> result.dat
   echo "fin $i xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
done
