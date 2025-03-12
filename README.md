# Spring Security JWT Authentication (Maven)

Proyek ini adalah contoh aplikasi Spring Boot yang mengimplementasikan autentikasi menggunakan Spring Security dan JSON Web Tokens (JWT), dibangun dengan Maven.

## Fitur

* Autentikasi pengguna menggunakan JWT.
* Otorisasi berbasis peran (role-based authorization).
* Penggunaan database H2 in-memory untuk pengembangan.
* Konfigurasi menggunakan `application.yml`.
* Pemasukan data mock menggunakan `insert.sql`.
* Log startup aplikasi yang informatif.

## Prasyarat

* Java 17 atau lebih tinggi.
* Maven.
* IDE (IntelliJ IDEA, Eclipse, dll.).

## Cara Menjalankan Aplikasi

1.  Clone repositori ini:

    ```bash
    git clone https://github.com/viditry/jwtspringboots.git
    ```

2.  Buka proyek di IDE Anda.

3.  Bangun proyek menggunakan Maven:

    ```bash
    mvn clean install
    ```

4.  Jalankan aplikasi:

    * Di IDE Anda, jalankan kelas `SpringSecurityJwtApplication`.
    * Atau, jalankan dari terminal:

        ```bash
        mvn spring-boot:run
        ```

5.  Aplikasi akan berjalan di `http://localhost:8012`.

## Konfigurasi

Konfigurasi aplikasi dapat ditemukan di `src/main/resources/application.yml`.

### Konfigurasi Penting

* `spring.datasource`: Konfigurasi database H2.
* `spring.jpa`: Konfigurasi JPA.
* `spring.sql.init.data-locations`: Lokasi file `insert.sql`.
* `jwt.secret`: Kunci rahasia JWT. **Penting:** Ganti dengan kunci rahasia yang aman.
* `jwt.expiration`: Waktu kedaluwarsa token JWT.

## Data Mock

Data mock untuk pengujian dimasukkan menggunakan `src/main/resources/insert.sql`. Pastikan untuk mengenkripsi kata sandi menggunakan `BCryptPasswordEncoder` sebelum memasukkannya ke `insert.sql`.

## Cara Menguji Autentikasi

1.  Gunakan alat seperti Postman atau cURL.
2.  Kirim permintaan POST ke `http://localhost:8080/authenticate` dengan body JSON:

    ```json
    {
        "username": "testuser",
        "password": "password"
    }
    ```

    atau

    ```json
    {
        "username": "admin",
        "password": "adminpassword"
    }
    ```

3.  Jika autentikasi berhasil, Anda akan menerima token JWT di respons.
4.  Gunakan token JWT di header `Authorization` (Bearer token) untuk mengakses endpoint yang dilindungi.

## Dependensi

Proyek ini menggunakan Maven sebagai alat build. Dependensi yang diperlukan didefinisikan dalam `pom.xml`.

## Kontribusi

Kontribusi dipersilakan. Silakan fork repositori ini dan buat pull request.

## Lisensi

Proyek ini dilisensikan di bawah lisensi [MIT](LICENSE).
