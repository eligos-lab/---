#!/bin/bash
echo "🔄 Building and running File Converter..."
mvn clean package exec:java -Dexec.mainClass=Main