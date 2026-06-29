#!/bin/bash
# Run the Hotel Reservation System
# Requires: Java 11+

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
OUT_DIR="$SCRIPT_DIR/out"

if [ ! -d "$OUT_DIR" ]; then
    echo "Project not compiled. Please compile first with:"
    echo "  javac -d out src/hotel/*.java"
    exit 1
fi

java -cp "$OUT_DIR" hotel.Main
