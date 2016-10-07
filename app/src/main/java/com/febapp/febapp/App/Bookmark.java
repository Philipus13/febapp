package com.febapp.febapp.App;

/**
 * Created by Philipus on 26/04/2016.
 */
public class Bookmark {
    private String imageUrl;
    private String name;
    private String rank;
    private int realName;
    private int createdBy;
    private int firstAppearance;
    private int total;
    private String id;
    private String profil;
    private String date;
    private Double lat;
    private Double lng;
    private String email;
    private String phone;
    private String alamat;
    private String web;
    private String youtube;
    private String instagram;
    private String fb;
    private String faq;
    private String kategori_acara;
    private String twitter;
    private String lokasi;
    private String start_date;
    private String end_date;
    private String slug;

    //private ArrayList<String> powers;

    public Bookmark(){

    }
    public Bookmark(String lname, String gambar)
    {
        name = lname;
        imageUrl = gambar;

    }

    public  Double getLat() {return lat;}

    public void setLat(Double lat) {this.lat = lat; }


    public  Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDate() {return date;}

    public String setDate(String lat) {this.date = date;
        return date;
    }

    public  String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Getters and Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //GET AND SET JUDUL
    public String getJudul() {
        return name;
    }

    public void setJudul(String name) {
        this.name = name;
    }

    //GET AND SET DESKRIPSI
    public String getDeskripsi() {
        return rank;
    }

    public void setDeskripsi(String rank) {
        this.rank = rank;
    }

    //GET AND SET DUIT

    public int getDuit() {
        return realName;
    }

    public void setDuit(int realName) {
        this.realName = realName;
    }

    //GET AND SET PERSEN

    public int getPersen() {
        return createdBy;
    }

    public void setPersen(int createdBy) {
        this.createdBy = createdBy;
    }

    //GET AND SET SISA HARI

    public int getSisaHari() {
        return firstAppearance;
    }

    public void setSisaHari(int firstAppearance) {
        this.firstAppearance = firstAppearance;
    }
    public int  getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getFaq() {
        return faq;
    }

    public void setFaq (String faq) {
        this.faq = faq;
    }

    public String getKategori_acara() {
        return kategori_acara;
    }

    public void setKategori_acara(String kategori_acara) {
        this.kategori_acara = kategori_acara;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


}

