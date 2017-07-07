# siranap-post
Integrasi Data dengan Bed Monitoring Yankes Kemenkes

Mengirimkan Data keadaan tempat tidur dari SIMRS Ke Web Service Ditjen Pelayanan Kesehatan.
Merujuk pada juknis pada http://sirs.yankes.kemkes.go.id/sirsservice/start/ts
bagian Bed Monitoring.

Menggunakan Java 1.8 (Java™ Platform, Standard Edition 8)

Cara menggunakan:
1. Download project https://github.com/dnaftali/siranap-post
2. Generate file "siranap.sql" pada server MySQL.
3. Edit credential dan header pada file  "/src/siranappost/SiranapPOST.java"
4. Compile menjadi jar.
5. Jalankan menggunakan perintah: java -jar  /dist/SiranapPOST.jar
