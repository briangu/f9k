#!/bin/bash
mvn install:install-file -Dfile=lib/simplenlgv4.3.jar -DgroupId=simplenlg -DartifactId=simplenlg -Dversion=4.3.0 -Dpackaging=jar
# lexicon/lexAccess2011lite/lib/lexAccess2011dist.jar
mvn install:install-file -Dfile=lexicon/lexAccess2011lite/lib/lexAccess2011dist.jar -DgroupId=lexAccess2011lite -DartifactId=lexAccess2011lite -Dversion=1.0.0 -Dpackaging=jar


