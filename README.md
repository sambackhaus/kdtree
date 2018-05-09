
# overview

## tesla-nn-data
Zeppelin:
```scala
import scala.collection.mutable.ListBuffer
import java.text.DecimalFormat
val decimalFormat: DecimalFormat = new DecimalFormat("0.##########")
val tracklogStrings = sc.textFile("/tesla/live/views_and_orders/000303/output/als/withBasketsAndWishlists/product_features")
val vectorStrings = tracklogStrings.map(_.split(":").last.split(",").map(x=>decimalFormat.format(x.toDouble).toString).toArray.mkString("   "))
vectorStrings.coalesce(1,true).saveAsTextFile("/tesla/live/zeppelin/extract_article_vectors_christian")

import scala.util.Random
sc.parallelize(Random.shuffle(vectorStrings.take(200000).toList).take(200)).coalesce(1,true).saveAsTextFile("/tesla/live/zeppelin/query_vectors_christian")
```

# nmslib
Faiss is according to them one of the best similarity query libraries:

https://code.facebook.com/posts/1373769912645926/faiss-a-library-for-efficient-similarity-search/

which is a fast library for querying billions of vectors, there is a reference for an even faster tool if there are only one million vectors (Non-Metric Space Library):
    
https://github.com/nmslib/nmslib
    
They provide a very good documentation:

https://github.com/nmslib/nmslib/blob/master/manual/manual.pdf

nmslib is written in c++  and one can setup a query-server to access the server in java.
For this one needs the translation tool (or whatever develpers call this stuff) which is provided by Thrift:

## query server
https://thrift.apache.org/docs/BuildingFromSource

execute
    ./query_server -i ../../sample_data/final8_10K.txt -s l2 -m sw-graph -c NN=10,efConstruction=200 -p 10000
query 
    head -1 ../../sample_data/final8_10K.txt | ./query_client -p 10000 -a localhost  -k 10

## nmslib experiment
The library has its own experimentation framework. Use the script located here:

```bash
/nmslib/similarity_search/release/experiment
```
There is also a script t run all methods here use, e.g.,  
```bash
./multi_experiment.sh ../../nmslib/similarity_search/release/experiment  ../target/test_data.tsv ../target/query_data.tsv
```
## nmslib scripts (in nmslib package)

will make a test of some methods and even plot, use, e.g.,
```bash
./test_run.sh ../../sandbox/target/test_data.tsv l1 1 5
```