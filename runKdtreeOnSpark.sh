#!/bin/sh

sbt assembly && spark-submit \
--class de.gwik.kdtree.KdtreeTest \
--master yarn \
--deploy-mode cluster \
--driver-cores 2 \
--driver-memory 4g \
--num-executors 20 \
--executor-cores 2 \
--executor-memory 8g \
target/scala-2.11/sandbox-assembly-0.1.jar \