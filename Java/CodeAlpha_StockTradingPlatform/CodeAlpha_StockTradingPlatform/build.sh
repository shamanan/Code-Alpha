#!/bin/bash
mkdir -p out
javac -d out src/trading/*.java && echo "Build successful! Run with: ./run.sh"
