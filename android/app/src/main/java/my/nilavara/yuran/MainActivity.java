package my.nilavara.yuran;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.webkit.WebViewAssetLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Aplikasi Yuran Nilavara: memaparkan index.html (disalin ke assets semasa bina)
 * dalam WebView, dengan jambatan "Android" untuk simpan fail dan cetak.
 */
public class MainActivity extends Activity {
    private static final String HOST = "appassets.androidplatform.net";
    private static final String START_URL = "https://" + HOST + "/assets/index.html";
    private static final String FOLDER = "YuranNilavara";
    private static final int REQ_FILE = 101;
    private WebView web;
    private ValueCallback<Uri[]> fileCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setSupportMultipleWindows(false);

        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest r) {
                return loader.shouldInterceptRequest(r.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri u = r.getUrl();
                if (HOST.equals(u.getHost())) return false;
                // Pautan luar (cth. WhatsApp) dibuka dalam aplikasi berkaitan
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, u));
                } catch (Exception e) {
                    toast("Tiada aplikasi untuk membuka pautan ini");
                }
                return true;
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = cb;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                String[] types = p.getAcceptTypes();
                i.setType(types != null && types.length > 0 && types[0].startsWith("image") ? "image/*" : "*/*");
                try {
                    startActivityForResult(Intent.createChooser(i, "Pilih fail"), REQ_FILE);
                } catch (Exception e) {
                    fileCallback = null;
                    return false;
                }
                return true;
            }
        });

        web.addJavascriptInterface(new Bridge(), "Android");

        if (Build.VERSION.SDK_INT <= 28 &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (savedInstanceState != null) web.restoreState(savedInstanceState);
        else web.loadUrl(START_URL);
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req != REQ_FILE || fileCallback == null) return;
        Uri[] result = null;
        if (res == RESULT_OK && data != null && data.getData() != null) result = new Uri[]{data.getData()};
        fileCallback.onReceiveValue(result);
        fileCallback = null;
    }

    @Override
    public void onBackPressed() {
        // Tutup modal / balik ke Utama dahulu; jika sudah di Utama, keluar aplikasi
        web.evaluateJavascript("window.__back?window.__back():0",
                v -> { if (!"1".equals(v)) MainActivity.super.onBackPressed(); });
    }

    private void toast(String m) {
        runOnUiThread(() -> Toast.makeText(this, m, Toast.LENGTH_LONG).show());
    }

    private static String safeName(String n) {
        String s = n == null ? "fail" : n.replaceAll("[\\\\/:*?\"<>|]", "_");
        return s.isEmpty() ? "fail" : s;
    }

    class Bridge {
        /** Simpan fail (backup .json, laporan .xlsx/.csv) ke folder Download/YuranNilavara. */
        @JavascriptInterface
        public void saveFile(String name, String b64, String mime) {
            try {
                byte[] bytes = Base64.decode(b64, Base64.DEFAULT);
                String fn = safeName(name);
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues cv = new ContentValues();
                    cv.put(MediaStore.Downloads.DISPLAY_NAME, fn);
                    cv.put(MediaStore.Downloads.MIME_TYPE, mime);
                    cv.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/" + FOLDER);
                    Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv);
                    if (uri == null) throw new Exception("Tidak dapat cipta fail");
                    try (OutputStream os = getContentResolver().openOutputStream(uri)) { os.write(bytes); }
                } else {
                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), FOLDER);
                    dir.mkdirs();
                    try (FileOutputStream os = new FileOutputStream(new File(dir, fn))) { os.write(bytes); }
                }
                toast("Disimpan: Download/" + FOLDER + "/" + fn);
            } catch (Exception e) {
                toast("Gagal simpan: " + e.getMessage());
            }
        }

        /** Buka dialog cetak Android (boleh pilih printer atau Simpan sebagai PDF). */
        @JavascriptInterface
        public void print(String title, String paper) {
            runOnUiThread(() -> {
                PrintManager pm = (PrintManager) getSystemService(PRINT_SERVICE);
                PrintDocumentAdapter ad = web.createPrintDocumentAdapter(title);
                PrintAttributes.MediaSize size = "80".equals(paper)
                        ? new PrintAttributes.MediaSize("RESIT80", "Resit 80mm", 3150, 11000)
                        : PrintAttributes.MediaSize.ISO_A4;
                PrintAttributes attr = new PrintAttributes.Builder().setMediaSize(size).build();
                pm.print(title, ad, attr);
            });
        }
    }
}
