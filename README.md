# Sistem Yuran Nilavara

Sistem kutipan yuran *standalone* — satu fail `index.html`, tiada server, tiada pemasangan. Tema editorial lembut (lavender + emas) dengan logo Nilavara.

## Pasang sebagai aplikasi

### Telefon Android (APK)
1. Buka pautan ini di telefon (log masuk GitHub jika diminta):
   **https://github.com/Blackburn-code/sistem-yuran-standalone/releases/latest/download/Yuran-Nilavara.apk**
2. Buka fail `Yuran-Nilavara.apk` yang dimuat turun → **Pasang**.
   Jika diminta, benarkan *Pasang aplikasi tidak diketahui* untuk pelayar/Fail.
3. Ikon **Yuran Nilavara** muncul di skrin utama.

APK dibina semula secara automatik setiap kali kod dalam `main` dikemas kini.
Fail backup & laporan Excel disimpan dalam folder **Download/YuranNilavara**.
Selepas kunci tetap disediakan (lihat bahagian pembangun), versi baharu boleh dipasang terus atas versi lama dan **data kekal**.

### PC (Chrome / Edge)
Perlukan GitHub Pages (**Settings → Pages → Branch: main → Save**). Untuk repo *private*, Pages memerlukan pelan GitHub berbayar.
1. Buka pautan Pages dalam Chrome/Edge.
2. Klik ikon **Install** (⊕) di hujung bar alamat.
3. Ikon **Yuran Nilavara** muncul di Desktop / Start Menu, dan boleh digunakan tanpa internet.

Tanpa Pages: muat turun ZIP repo, extract, dan buka `index.html`.

## Cara guna (fail terus)

1. Muat turun / klon repo ini.
2. Buka `index.html` dalam Chrome, Edge atau Safari (dwiklik sahaja).
3. Data demo dimuatkan pada kali pertama. Kosongkan di **Tetapan → Kosongkan semua data** bila sedia guna sebenar.

Berfungsi sepenuhnya tanpa internet (pustaka QR, Excel dan fon disimpan dalam `assets/`).

## Ciri

| Halaman | Fungsi |
|---|---|
| Utama | Kutipan bulan/tahun, jumlah tunggakan, carta 12 bulan, bayaran terkini |
| Bayaran | Pilih ahli → yuran tertunggak ditanda automatik, bayaran pendahuluan, item lain, ubah amaun (separa/diskaun) |
| Ahli | Daftar, cari, tapis kumpulan, tetapkan yuran setiap orang |
| Tunggakan | Senarai automatik + butang **WhatsApp** dengan mesej peringatan siap |
| Resit | Cetak semula **A4** atau **80mm thermal**, hantar WhatsApp, batal (dengan sebab), semak keaslian |
| Laporan | Ikut tempoh, ikut jenis yuran & kaedah, **eksport Excel** (4 helaian) atau cetak |
| Tetapan | Profil organisasi, logo, label (Ahli/Pelajar/Murid…), awalan resit, templat WhatsApp, jenis yuran, backup/pulih |

### Resit
- Nombor berturutan setahun: `NLV-2026-00001`
- Amaun dalam perkataan (Ringgit Malaysia … Sahaja)
- Kod QR + *kod sah* 6 aksara untuk semakan di halaman Resit
- Resit batal kekal dalam rekod dengan cop **BATAL**

## Penting
- Data disimpan dalam pelayar (localStorage) **pada peranti itu sahaja**. Buat **backup** selalu (Tetapan → Muat turun backup) dan simpan di Drive/emel.
- Kod sah pada QR ialah semakan asas untuk mengesan resit yang diubah, bukan tandatangan digital.
- Untuk guna ramai pengguna serentak / beberapa peranti, perlu versi server (fasa seterusnya).

## Untuk pembangun
- `android/` — projek Android (WebView). Workflow `.github/workflows/build-apk.yml` menyalin `index.html`, `assets/`, `icons/` ke dalam APK, membina, dan menerbitkan ke Releases (tag `apk-terkini`) bila `main` dikemas kini.
- Kunci tandatangan tetap dibaca daripada GitHub Secrets: `APK_KEYSTORE_BASE64` (fail keystore dalam base64, alias `yuran`) dan `APK_KEY_PASSWORD`. Tanpanya, APK guna kunci debug sementara dan setiap kemas kini perlu *uninstall* dahulu (buat backup sebelum itu).
- Bila `index.html` dikemas kini, tukar `VERSI` dalam `sw.js` supaya versi PC (Pages) mendapat kemas kini.
