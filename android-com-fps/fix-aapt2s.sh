#!/bin/bash
echo "[fix-aapt2] Forcing local AAPT2..."

CACHE_DIR="$HOME/.gradle/caches"

find "$CACHE_DIR" -type f -name "aapt2*" | while read -r f; do
    echo "[fix-aapt2] Replacing $f"
    cp ./aapt2 "$f"
    chmod +x "$f"
done

echo "[fix-aapt2] Done."
