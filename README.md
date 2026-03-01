## 🚀 Deployment URL
Akses website di sini: [advshop](https://natural-arabella-acid-workspace-1a11f830.koyeb.app/product/list)
# Reflection 1 (CODING STANDARDS)
- **Secure Coding**: 
Aku melakukan pengecekan null untuk menghindari `NullPointerException` dan memastikan ID produk selalu valid sebelum melakukan operasi hapus atau edit.
- **Clean Code:** 
Penamaan variabel dan metode yang jelas (`productData`, `delete`, `createProductPost`), metode yang memiliki satu tanggung jawab saja, serta kode yang mudah dibaca dan dimaintain
- **Proses try & error**  
  Saat membuat fitur baru, saya melakukan beberapa percobaan untuk memastikan tombol Edit dan Delete bekerja dengan baik.  
  Beberapa error muncul, seperti tombol Edit tidak mengarah ke halaman yang tepat dan routing Delete salah.  

- **Perbaikan masalah Edit**  
  Ternyata penyebab masalah ini terjadi adalah karena ketika createProduct berjalan, fungsi ini tidak membuat sebuah string id random untuk tiap productnya, sehingga aku menambahkan code untuk
  generate random id, agar ketika fungsi nya routing ke /product/list/{id}, id nya menjadi valid dan tidak berisi null. Sehingga bisa routing ke fungsi editproduct.

- **Perbaikan masalah Delete**  
  Mengubah redirect dari `"redirect:product/list"` menjadi `"redirect:/product/list"` dengan menambahkan garis miring di depannya.  
  Ternyata salah taruh "/" aja dapat menyebabkan masalah yang fatal.
  
# Reflection 2 (CODING STANDARDS)
## Unit test
- `How many unit tests should be made in a class?`
Menurutku tidak ada angka pasti dalam unit test, tapi selama itu mencakup semua skenario. Skenario positif, skenario negatif, skenario null, dll. maka akan menghasilkan code coverage yang semakin tinggi dan membuat kode kita lebih "kokoh" dan tidak rentan terhadap bug. Meskipun tidak menjamin akan 100% bebas dari bug.

- `How to make sure that our unit tests are enough to verify our program?`
Menurutku caranya adalah dengan memastikan code coverage setinggi mungkin dan Testing manual semua kemungkinannya, yaitu dengan cara akses langsung programnnya dan bruteforce semua kemungkinan input, skenario, dll. Untuk mencoba semua kemungkinan error.

- `If you have 100% code coverage, does
that mean your code has no bugs or errors?`
Tidak menjamin bahwa tidak ada error, kita tetap harus testing manual semua kemungkinan input, skenario, dll. secara bruteforce untuk mencari logika yang salah. Tapi code coverage membuat kita lebih yakin bahwa semua kodenya (yang telah tercoverage) telah teruji secara unit testing (programly testing).

- `What do you think about the cleanliness of the code of the new functional test suite? Will
the new code reduce the code quality?`
Pembuatan kelas baru dengan menyalin struktur yang sama akan membuat kode menjadi unclean. Hal ini secara langsung akan menurunkan kualitas kode secara keseluruhan karena menciptakan pengulangan yang tidak perlu.

  Pembuatan unit test baru dengan copy struktur yang sama akan menyebabkan duplikasi kode yang melanggar prinsip DRY (Don't Repeat Yourself) dan mempersulit maintanance di masa depan.

  Untuk memperbaikinya menurutku adalah prosedur setup dan variabel umum harus dikumpulkan ke dalam sebuah Base Class yang kemudian diwariskan ke setiap kelas tes spesifik. Ini akan membuat kode lebih bersih, mudah dibaca, dan lebih efisien untuk dikembangkan.

# Reflection 3 (CI/CD & DevOps)

### 1. Code Quality Issues and Fix Strategy
Selama pengerjaan tutorial dan latihan ini, saya menemukan dan memperbaiki beberapa masalah terkait kualitas kode dan infrastruktur:

* **Gradle Execution Permission:**
    * **Masalah:** Pada alur CI (`ci.yml`), proses *unit test* gagal dijalankan karena file `gradlew` tidak memiliki izin eksekusi (*permission denied*).
    * **Strategi Perbaikan:** Saya menambahkan perintah `chmod +x gradlew` dalam langkah-langkah *workflow* GitHub Actions sebelum menjalankan perintah pengujian. Hal ini memastikan *runner* CI memiliki izin untuk mengeksekusi *wrapper* Gradle.
* **PMD Failure & Test Coverage:**
    * **Masalah:** Analisis kode statis menggunakan PMD gagal karena ditemukan beberapa pelanggaran standar kode dan cakupan pengujian (*test coverage*) yang belum memadai.
    * **Strategi Perbaikan:** Saya meninjau kembali laporan PMD, menghapus variabel yang tidak digunakan, serta meningkatkan jumlah *unit test* untuk mencakup skenario negatif. Dengan meningkatkan *test coverage*, integritas kode lebih terjaga dan lolos dari standar kualitas yang ditetapkan.
* **Deployment Configuration:**
    * **Masalah:** Aplikasi berhasil di-*build* tetapi gagal saat tahap *deployment* di Koyeb karena adanya kesalahan konfigurasi port (TCP health check failed).
    * **Strategi Perbaikan:** Saya menyesuaikan pengaturan di Dashboard Koyeb agar melakukan pengecekan pada port `8080` (sesuai dengan port default Spring Boot) dan memastikan *Environment Variables* telah terkonfigurasi dengan benar.

---

### 2. CI/CD Consistency Reflection
Berdasarkan implementasi alur kerja (workflow) pada GitHub Actions saat ini, saya berpendapat bahwa proyek ini **sudah memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD)**. 

Alasannya adalah:
1.  **Continuous Integration:** Setiap kali saya melakukan *push* atau *pull request* ke repositori, GitHub Actions secara otomatis menjalankan *workflow* `ci.yml` yang mencakup proses *build* dan *automated testing*. Hal ini memastikan bahwa kode baru selalu terintegrasi dengan kode lama tanpa merusak fungsi yang sudah ada.
2.  **Continuous Deployment:** Implementasi ini telah terhubung langsung dengan platform PaaS (Koyeb). Setelah kode lolos semua tahapan pengujian dan analisis kualitas di branch yang ditentukan, aplikasi akan langsung diperbarui di server tanpa intervensi manual, sehingga perubahan dapat langsung dirasakan oleh pengguna.
3.  **Automated Quality Gate:** Adanya integrasi alat analisis seperti PMD dan Scorecard memastikan bahwa setiap perubahan kode tidak hanya "berjalan", tetapi juga memenuhi standar keamanan dan kebersihan kode yang baik sebelum dianggap layak untuk dideploy.

# Reflection 4 (SOLID PRINCIPLES)
### 1. Prinsip SOLID yang Diterapkan pada Proyek
Berdasarkan perubahan dari kode sebelumnya ke kode yang baru, terdapat beberapa prinsip SOLID yang telah saya terapkan:
* **Single Responsibility Principle (SRP):** Saya memisahkan kelas `CarController` yang sebelumnya menumpuk di dalam file `ProductController.java` menjadi file dan kelas terpisah (`CarController.java`). Kini masing-masing controller hanya memiliki satu tanggung jawab (mengurus Product atau mengurus Car).
* **Liskov Substitution Principle (LSP):** Saya menghapus pewarisan (`extends ProductController`) pada kelas `CarController`. Sebuah *CarController* pada dasarnya bukanlah *subclass* yang valid dari *ProductController* karena memiliki sifat dan *endpoint* yang berbeda. Menghapus pewarisan ini membuat hierarki kode menjadi lebih benar dan sesuai logika.
* **Dependency Inversion Principle (DIP):** Pada `CarController`, saya mengubah tipe data *dependency injection* dari yang sebelumnya bergantung pada kelas konkret `CarServiceImpl` menjadi bergantung pada antarmuka/abstraksi `CarService`.
* **Open-Closed Principle (OCP):** Karena di poin DIP saya sudah menggunakan interface CarService (bukan implementasi konkretnya), kode saya sekarang terbuka untuk ekstensi tapi tertutup untuk modifikasi (open for extension, but closed for modification). 

### 2. Keuntungan Menerapkan Prinsip SOLID (dengan Contoh)
* **Lebih mudah di-maintain dan di-test (Maintainability):** Dengan memisahkan `CarController` ke filenya sendiri (SRP), file menjadi lebih kecil dan fokus. Jika saya menemukan *bug* di fitur *Car*, saya bisa memperbaikinya tanpa risiko tidak sengaja merusak fitur *Product*.
* **Fleksibilitas Tinggi (Flexibility/Extensibility):** Dengan menerapkan DIP (menggunakan `private CarService carservice;` alih-alih `CarServiceImpl`), saya dapat dengan mudah menukar implementasinya di masa depan. Contohnya, jika nanti saya ingin beralih dari penyimpanan *in-memory list* ke *database server*, saya cukup membuat kelas baru (misal: `CarServiceDatabaseImpl` yang mengimplementasi `CarService`) tanpa perlu menyentuh satu baris pun kode di `CarController`.
* **Mencegah Side-Effects yang Tidak Terduga:** Dengan menghapus `extends ProductController` dari `CarController` (LSP), saya mencegah `CarController` secara tidak sengaja mewarisi atau mengekspos *endpoint* milik `ProductController`, sehingga *routing* menjadi bersih.

### 3. Kerugian Jika Tidak Menerapkan Prinsip SOLID (dengan Contoh)
* **Coupling yang Tinggi (Tightly Coupled):** Saat `CarController` masih bergantung pada kelas konkret `CarServiceImpl`, kode menjadi kaku. Jika struktur konstruktor `CarServiceImpl` berubah, `CarController` mungkin juga harus ikut diubah. Ini membuat kode rentan patah (*fragile*).
* **God Object / Spaghetti Code:** Menyimpan `CarController` di dalam file `ProductController.java` akan membuat file tersebut membengkak ukurannya sering bertambahnya fitur aplikasi. Ketika aplikasi sudah berskala besar, file tersebut akan memiliki ratusan atau ribuan baris kode yang sangat sulit untuk dibaca dan dikerjakan secara paralel dengan anggota tim lain akibat *merge conflict*.
* **Pewarisan yang Membingungkan:** Memaksa `CarController extends ProductController` hanya agar bisa berbagi sedikit *behavior* akan mengacaukan struktur OOP. Hal ini bisa menyebabkan metode dari `ProductController` terpanggil di alur mobil yang berpotensi menghasilkan *bug* fatal pada data *production*.
