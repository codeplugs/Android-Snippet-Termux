#!/bin/bash

# Log awal
echo "[*] Mulai menjalankan fix-aapt2.sh" > /tmp/fix-aapt2.log

# Menemukan semua file aapt2-*-linux.jar di cache Gradle
AAPT2_JAR_FILES=$(find ~/.gradle -name 'aapt2-*-linux.jar' -type f)

if [ -z "$AAPT2_JAR_FILES" ]; then
    echo "[!] Tidak ditemukan file aapt2-*-linux.jar di cache Gradle." >> /tmp/fix-aapt2.log
    echo "[!] Pastikan bahwa cache Gradle sudah ada." >> /tmp/fix-aapt2.log
    exit 1
else
    echo "[*] Ditemukan file aapt2 di cache Gradle." >> /tmp/fix-aapt2.log
fi

# Menjalankan perintah jar untuk menambahkan file aapt2 ke dalam jar
echo "[*] Menambahkan aapt2 ke dalam jar di /usr/bin..." >> /tmp/fix-aapt2.log

# Menambahkan aapt2 ke dalam file jar yang ditemukan
echo "$AAPT2_JAR_FILES" | xargs -I{} jar -u -f {} -C . aapt2

# Log akhir
echo "[*] Selesai menjalankan fix-aapt2.sh" >> /tmp/fix-aapt2.log
