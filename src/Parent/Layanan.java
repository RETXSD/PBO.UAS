package Parent;

public class Layanan {
    private String jenis;
    private int harga;
    public String estimasi_waktu;
    public String kategori;


    public Layanan(String jenis, int harga, String estimasi_waktu, String kategori) {
        this.jenis = jenis;
        this.harga = harga;
        this.estimasi_waktu = estimasi_waktu;
        this.kategori = kategori;
    }

    public Layanan(String jenis, int harga, String estimasi_waktu) {
        this.jenis = jenis;
        this.harga = harga;
        this.estimasi_waktu = estimasi_waktu;
        
    }

    public String getJenis() {
        return jenis;
    }
    public int getHarga() {
        return harga;
    }
    public String getEstimasiWaktu() {
        return estimasi_waktu;
    }

    public String getKategori() {
        return kategori;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
    public void setHarga(int harga) {
        this.harga = harga;
    }
    public void setEstimasiWaktu(String estimasi_waktu) {
        this.estimasi_waktu = estimasi_waktu;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
