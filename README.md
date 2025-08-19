# RS Online Sync

Aplikasi Java 21 Console untuk melakukan sinkronisasi data tempat tidur dari database rumah sakit ke endpoint API Kemenkes.

## Deskripsi

Aplikasi ini dirancang untuk:
- Mengambil data tempat tidur dari API Kemenkes (GET)
- Mengirim data tempat tidur ke API Kemenkes (POST)
- Memperbarui data tempat tidur di API Kemenkes (PUT)
- Sinkronisasi data dari database Oracle rumah sakit

## Fitur yang Tersedia

### ✅ Tahap 1 - GET Data Tempat Tidur
- Melakukan request GET ke endpoint API Kemenkes
- Menampilkan response dalam format JSON yang rapi
- Header authentication sesuai requirement Kemenkes

### ✅ Tahap 2 - DELETE Data Tempat Tidur
- Melakukan GET semua data tempat tidur dari API Kemenkes
- Parsing response dan ekstrak semua id_t_tt
- Looping delete setiap data berdasarkan id_t_tt
- **Opsi untuk menampilkan detail JSON data yang akan dihapus**
- Progress tracking dan summary hasil operasi
- Konfirmasi ganda untuk keamanan
- Response JSON selalu ditampilkan untuk setiap operasi

### ✅ Tahap 3 - POST Data Tempat Tidur ke Kemenkes
- Koneksi ke database Oracle menggunakan query dari context.txt
- Query data tempat tidur dari database
- Convert data database ke format JSON API Kemenkes
- Looping POST setiap data ke API Kemenkes
- **Opsi untuk menampilkan detail JSON data yang dikirim**
- Progress tracking dan summary hasil operasi
- Response JSON selalu ditampilkan untuk setiap operasi

### 🔄 Tahap Selanjutnya (akan diimplementasikan)
- PUT/Update data tempat tidur
- Sinkronisasi dari database Oracle

## Persyaratan Sistem

- Java 21 atau lebih tinggi
- Maven 3.6+
- Koneksi internet untuk akses API Kemenkes
- Koneksi database Oracle (untuk fitur sinkronisasi)

## Konfigurasi

### Credential API Kemenkes
- Base URL: `https://sirs.kemkes.go.id/fo/index.php/`
- X-rs-id: `3404015` (kode faskes)
- X-pass: `S4rdj1t@!` (password bridging)

### Database Oracle
- Host: `10.100.254.8:1521`
- Database: `DBPROD`
- User: `SIMRSLIVE`
- Password: `Xk9ReNheBbTb7Nf5`

## Cara Menjalankan

### 1. Clone Repository
```bash
git clone <repository-url>
cd RS-Online
```

### 2. Build Project
```bash
mvn clean compile
```

### 3. Run Application
```bash
mvn exec:java -Dexec.mainClass="com.rsonline.Main"
```

### 4. Build JAR Executable
```bash
mvn clean package
java -jar target/rs-online-sync-1.0.0.jar
```

## Struktur Project

```
src/main/java/com/rsonline/
├── Main.java                 # Entry point aplikasi dengan menu console
├── Config.java               # Konfigurasi credential dan URL
├── KemenkesApiClient.java    # HTTP client untuk API Kemenkes
├── JsonFormatter.java        # Utility untuk format JSON
├── BedDataParser.java        # Parser untuk data tempat tidur
├── OracleDatabaseClient.java # Client untuk database Oracle
├── BedDataConverter.java     # Converter data database ke JSON Kemenkes
└── ResponseParser.java       # Parser response dari API Kemenkes
```

## Menu Aplikasi

1. **GET Data Tempat Tidur dari Kemenkes** - Mengambil data dari API
2. **POST Data Tempat Tidur ke Kemenkes** - Sync dari database Oracle → Convert → Loop POST
3. **PUT/Update Data Tempat Tidur** - Memperbarui data (akan diimplementasikan)
4. **DELETE Data Tempat Tidur dari Kemenkes** - Hapus semua data (GET → Parse → Loop Delete)
5. **Sinkronisasi dari Database Oracle** - Sync dari database (akan diimplementasikan)
6. **Keluar** - Keluar dari aplikasi

## API Endpoints

### GET Data Tempat Tidur
- **URL**: `https://sirs.kemkes.go.id/fo/index.php/Fasyankes`
- **Method**: GET
- **Headers**:
  - `X-rs-id`: Kode faskes
  - `X-pass`: Password bridging
  - `X-Timestamp`: Timestamp saat ini
  - `Accept`: application/json

### POST Data Tempat Tidur
- **URL**: `https://sirs.kemkes.go.id/fo/index.php/Fasyankes`
- **Method**: POST
- **Body**: JSON dengan field tempat tidur

### PUT Data Tempat Tidur
- **URL**: `https://sirs.kemkes.go.id/fo/index.php/Fasyankes`
- **Method**: PUT
- **Body**: JSON dengan field tempat tidur termasuk `id_t_tt`

### DELETE Data Tempat Tidur
- **URL**: `https://sirs.kemkes.go.id/fo/index.php/Fasyankes?id_t_tt={id_t_tt}`
- **Method**: DELETE
- **Query Parameter**: `id_t_tt` - ID tempat tidur yang akan dihapus

## Format Data Tempat Tidur

```json
{
  "id_tt": "3",
  "ruang": "Irina A atas (Bedah)",
  "jumlah_ruang": "3",
  "jumlah": "7",
  "terpakai": "3",
  "terpakai_suspek": "0",
  "terpakai_konfirmasi": "0",
  "antrian": "2",
  "prepare": "0",
  "prepare_plan": "0",
  "covid": 0,
  "terpakai_dbd": "3",
  "terpakai_dbd_anak": "3",
  "jumlah_dbd": "9"
}
```

## Troubleshooting

### Error Koneksi API
- Periksa koneksi internet
- Verifikasi credential API Kemenkes
- Periksa apakah endpoint API aktif

### Error Database
- Periksa koneksi ke database Oracle
- Verifikasi credential database
- Periksa apakah service Oracle berjalan

## Dependencies

- **Apache HttpClient 5**: HTTP client untuk API calls
- **Jackson**: JSON processing
- **Oracle JDBC Driver**: Koneksi database Oracle
- **SLF4J**: Logging framework

## Lisensi

Project ini dibuat untuk keperluan internal rumah sakit.

## Kontak

Untuk pertanyaan atau support, silakan hubungi tim IT rumah sakit.
