# 🧾 Aplikasi Kasir — Java Swing Desktop (Tugas Akhir PBO)

Aplikasi kasir/POS desktop berbasis Java Swing dengan pemisahan hak akses Admin & Kasir, cetak struk, dan koneksi database MySQL — Tugas Akhir mata kuliah Pemrograman Berorientasi Objek (PBO).

## 📌 Deskripsi

Aplikasi desktop lengkap untuk transaksi kasir, dengan struktur kode yang rapi dan terorganisir per fitur (bukan satu file besar) — mencerminkan penerapan prinsip OOP dalam praktik.

## ✨ Fitur Utama (berdasarkan struktur source code)

- 🔐 **Login & Role** — `LoginPage` sebagai gerbang masuk, dengan halaman terpisah untuk **Admin** (`AdminPage`) dan **Kasir/User** (`UserPage`) — pemisahan hak akses yang jelas
- 🧾 **Cetak Struk** — modul `PrintStruk` khusus untuk mencetak bukti transaksi
- 🗄️ **Koneksi Database** — `Koneksi` mengelola koneksi ke database MySQL secara terpusat
- 🎨 **Splash Screen & Styling Kustom** — `splashscreen` saat aplikasi dibuka, `MainStyle` & `MainLogo` untuk tampilan yang konsisten
- 🧩 **Komponen UI Reusable** — `Komponen`, `JpanelKomponen`, `TabelKomponen`, `MainKomponen` — komponen Swing kustom yang dipakai ulang di berbagai halaman (praktik OOP yang baik: encapsulation & reusability)
- 📅 **Utilitas Tanggal** — modul `date` untuk format/kalkulasi tanggal transaksi

## ⚙️ Teknologi

- **Bahasa:** Java (Swing)
- **IDE/Project:** NetBeans (`nbproject/`, `build.xml`, Ant build system)
- **Database:** MySQL (folder `database/` berisi skema)

## 🚀 Cara Menjalankan

1. Buka project di **NetBeans IDE**.
2. Import skema database dari folder `database/` ke MySQL (mis. via phpMyAdmin/MySQL Workbench).
3. Sesuaikan kredensial koneksi database di kelas `Koneksi`.
4. Jalankan (`Run ▶`), atau build manual via `ant` menggunakan `build.xml`.


