# P3 Transformer Web Client

This allows to transform a resource on the web using any transformer. The
resource to transform and the transformer to use are specified via the query 
parameters `resource`and `transformer`.

Example:

http://localhost:8151/?transformer=http%3A%2F%2Fsandbox.fusepool.info%3A8192%2F%3Ftaxonomy%3Dhttp%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf&resource=http://www.bbc.com/news/science-environment-30005268


## Can one specify multiple transformers?

This tool only allows to specify one transformer. However the transformer
can be a [pipeline transformer](https://github.com/fusepoolP3/p3-pipeline-transformer)
which executed multiple transformers in sequence.

## What about asynchronous transformer? Is this service asynchronous too?

Regardless of whether the transformer handles the request synchronously or 
asynchronously this service will always respond synchronously. A consequence of
this is that it can take a long time till a request is answered.