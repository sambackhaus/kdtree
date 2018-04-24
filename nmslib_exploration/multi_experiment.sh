#!/bin/bash

path_to_experiment_script=$1
path_to_data=$2
path_to_queries=$3

output_prefix="out_"

declare -a arr=("vptree" "mvptree" "ghtree" "list_clusters" "satree" "bbtree" "lsh_multiprobe" "lsh_gaussian" "lsh_cauchy" "lsh_threshold" "proj_incsort" "proj_vptree" "omedrank" "pp-index" "mi-file" "napp" "perm_incsort_bin" "perm_bin_vptree" "sw-graph" "hnsw" "nndes" "seq_search" "mult_index")

for i in "${arr[@]}"
do
   echo "start $i xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
   $path_to_experiment_script -i $path_to_data -s l1 -q $path_to_queries -k 150 -m $i -o $output_prefix$i
   echo "fin $i xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
done