# P3 Transformer Web Client [![Build Status](https://travis-ci.org/fusepoolP3/p3-transformer-web-client.svg?branch=master)](https://travis-ci.org/fusepoolP3/p3-transformer-web-client)

This allows to transform a resource on the web using any Fusepool P3 transformer. The
resource to transform and the transformer to use are specified via the query 
parameters `resource`and `transformer`. Only one transformer can be specified, however 
the transformer can be a [pipeline transformer](https://github.com/fusepoolP3/p3-pipeline-transformer)
which executes multiple transformers in sequence.

Note: Regardless of whether the transformer handles the request synchronously or 
asynchronously this service will always respond synchronously. A consequence of
this is that it can take a long time until a request is answered.

## Try it out

The web client can be started using the latest release that can be downloaded from the releases section. The executable jar file contains all the necessary dependencies. To start the client, run the jar-file as follows:

 java -jar transformer-web-client-*-jar-with-dependencies.jar

The web client can then be accessed on port 8151 of your machine. As an example, using the dictionary-matcher-transformer running on FusepoolP3's infrastructure, try the following URL:

http://localhost:8151/?transformer=http%3A%2F%2Fsandbox.fusepool.info%3A8301%2F%3Ftaxonomy%3Dhttp%3A%2F%2Fdata.nytimes.com%2Fdescriptors.rdf&resource=http://www.bbc.com/news/science-environment-30005268

## Compiling and Running

After obtaining the sources, the web client can be built using maven:

 mvn install

To start the web client, use the following command:

 mvn exec:java

## Usage

See the example above.
