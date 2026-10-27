# Sistem Yuran Nilavara

Sistem kutipan yuran *standalone* — satu fail `index.html`, tiada server, tiada pemasangan. Tema editorial lembut (lavender + emas) dengan logo Nilavara.

## Cara guna

1. Muat turun / klon repo ini.
2. Buka `index.html` dalam Chrome, Edge atau Safari (dwiklik sahaja).
3. Data demo dimuatkan pada kali pertama. Kosongkan di **Tetapan → Kosongkan semua data** bila sedia guna sebenar.

Berfungsi tanpa internet (pustaka QR & Excel disimpan dalam `assets/vendor`). Fon Google hanya dimuat bila ada internet.

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
