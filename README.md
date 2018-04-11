Faiss is according to them one of the best similarity query libraries:

https://code.facebook.com/posts/1373769912645926/faiss-a-library-for-efficient-similarity-search/

which is a fast library for querying billions of vectors, there is a reference for an even faster tool if there are only one million vectors:
    
https://github.com/nmslib/nmslib
    
They provide a very good documentation:

https://github.com/nmslib/nmslib/blob/master/manual/manual.pdf

nmslib is written in c++  and one can setup a query-server to access the server in java.
For this one needs the translation tool (or whatever develpers call this stuff) which is provided by Thrift:

https://thrift.apache.org/docs/BuildingFromSource