#!/bin/bash
mkdir -p out
javac -d out src/hotel/*.java && echo "Build successful! Run with: ./run.sh"
