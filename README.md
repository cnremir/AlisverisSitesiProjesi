# 🛒 Açık Artırma Alışveriş Sistemi (Backend Odaklı)

Bu proje, kullanıcıların ürünleri açık artırma usulüyle satışa çıkarabildiği ve teklif verebildiği bir e-ticaret sistemidir. Uygulama, **Spring Boot** ile geliştirilmiş RESTful bir **backend mimarisi** üzerine kuruludur. Sistem; **kullanıcı**, **admin** ve **teklif** rollerini destekleyen çok katmanlı bir yapıya sahiptir.

---

## 🚀 Temel Özellikler (Kullanıcı Paneli)

- 🔐 **Basic Authentication** tabanlı kimlik doğrulama  
- 📦 **Ürün Açık Artırma Sistemi**  
  - Kullanıcı ürün ekleyebilir, mevcut ürünlere teklif verebilir  
  - Kullanıcı, sadece kendi eklediği ürünleri **profil sayfasında** görüntüleyebilir  
  - Her ürün için anlık **en yüksek teklif** dinamik olarak takip edilir  

---

## 🧾 Profil Sayfası Özellikleri

- Teklif verilen ürünleri filtreleyebilme  
- Kazanılan ürünlerin listelenmesi  
- En yüksek teklif verilen ürünlerin takip edilmesi  

---

## 🛡️ Admin Paneli Özellikleri

### ✅ Ürün Onay Sistemi
- Kullanıcıların eklediği ürünler admin onayından geçer  
- Admin, ürünleri **onaylayabilir** veya **reddedebilir**  

### 📊 Kapsamlı Takip Paneli
- Admin, tüm kullanıcıların **hangi ürünlere teklif verdiğini** görüntüleyebilir  
- Her ürün için **detaylı teklif geçmişine** erişim sağlar  

### 🗑️ Ürün Yönetimi
- Yayında olan ürünleri sistemden kaldırabilir  
- Onaysız ürünleri silebilir  

---

## 🧰 Kullanılan Teknolojiler

- Java & Spring Boot  
- Spring Security (Basic Auth)  
- Spring Data JPA – MySQL  
- RESTful API  
- Thymeleaf (temel düzeyde frontend)  
- Maven  
- Postman / Swagger  

