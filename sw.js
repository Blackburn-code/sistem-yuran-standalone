// Service worker: simpan fail sistem supaya boleh digunakan tanpa internet.
// Tukar VERSI setiap kali fail sistem dikemas kini supaya peranti mendapat versi baharu.
const VERSI = 'yuran-nilavara-v1';
const FAIL = ['./', './index.html', './manifest.json',
  './assets/logo-mark.png', './assets/vendor/qrcode.min.js', './assets/vendor/xlsx.mini.min.js',
  './assets/fonts/playfair-display-latin-500-normal.woff2', './assets/fonts/playfair-display-latin-600-normal.woff2',
  './assets/fonts/playfair-display-latin-500-italic.woff2', './assets/fonts/inter-latin-400-normal.woff2',
  './assets/fonts/inter-latin-500-normal.woff2', './assets/fonts/inter-latin-600-normal.woff2',
  './icons/icon-32.png', './icons/icon-180.png', './icons/icon-192.png', './icons/icon-512.png', './icons/icon-maskable-512.png'];

self.addEventListener('install', e => {
  e.waitUntil(caches.open(VERSI).then(c => c.addAll(FAIL)).then(() => self.skipWaiting()));
});
self.addEventListener('activate', e => {
  e.waitUntil(caches.keys()
    .then(k => Promise.all(k.filter(n => n !== VERSI).map(n => caches.delete(n))))
    .then(() => self.clients.claim()));
});
// Halaman: cuba internet dahulu (dapat versi terkini), jika gagal guna simpanan.
// Fail lain: guna simpanan dahulu.
self.addEventListener('fetch', e => {
  if (e.request.method !== 'GET') return;
  if (e.request.mode === 'navigate') {
    e.respondWith(fetch(e.request)
      .then(r => { const c = r.clone(); caches.open(VERSI).then(x => x.put('./index.html', c)); return r; })
      .catch(() => caches.match('./index.html')));
    return;
  }
  e.respondWith(caches.match(e.request).then(r => r || fetch(e.request)));
});
