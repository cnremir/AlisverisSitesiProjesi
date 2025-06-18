package com.alisveris.AlisverisSitesi;

import com.alisveris.AlisverisSitesi.models.Category;
import com.alisveris.AlisverisSitesi.models.SubCategory;
import com.alisveris.AlisverisSitesi.repository.CategoryRepository;
import com.alisveris.AlisverisSitesi.repository.ProductRepository;
import com.alisveris.AlisverisSitesi.repository.SubCategoryRepository;
import com.alisveris.AlisverisSitesi.services.CategoryService;
import com.alisveris.AlisverisSitesi.services.ProductServices;
import com.alisveris.AlisverisSitesi.services.SubCategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class AlisverisSitesiApplication implements CommandLineRunner {
    private final ProductServices productServices;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    public AlisverisSitesiApplication(ProductRepository productRepository, CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, ProductServices productServices, CategoryService categoryService, SubCategoryService subCategoryService) {

        this.productServices = productServices;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
    }


    public static void main(String[] args) {

        SpringApplication.run(AlisverisSitesiApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        kategoriEkle();
        subCategoryEkle();
    }


    public void kategoriEkle() {
        Category category1 = new Category();
        category1.setCategoryId(6);
        category1.setCategoryName("Giyim");
        Category category2 = new Category();
        category2.setCategoryId(2);
        category2.setCategoryName("Moda");
        Category category3 = new Category();
        category3.setCategoryId(3);
        category3.setCategoryName("Ev ve Yaşam");
        Category category4 = new Category();
        category4.setCategoryId(4);
        category4.setCategoryName("Kozmetik ve Kişisel Bakım");
        Category category5 = new Category();
        category5.setCategoryId(5);
        category5.setCategoryName("Spor ve Outdoor");


        List<Category> categoryList = Arrays.asList(category1, category2, category3, category4, category5);
        categoryService.addCategory(categoryList);
    }
    public void subCategoryEkle(){
        // Elektronik Alt Kategoriler
        List<String> elektronik = Arrays.asList(
                "Cep Telefonları",
                "Televizyonlar",
                "Kameralar",
                "Aksesuarlar"
        );

// Moda Alt Kategoriler
        List<String> moda = Arrays.asList(
                "Kadın Giyim",
                "Erkek Giyim",
                "Ayakkabılar",
                "Çantalar",
                "Aksesuarlar"
        );

// Ev & Yaşam Alt Kategoriler
        List<String> evYasam = Arrays.asList(
                "Mobilya",
                "Dekorasyon",
                "Mutfak Gereçleri",
                "Banyo Ürünleri",
                "Bahçe & Outdoor"
        );

// Kozmetik & Kişisel Bakım Alt Kategoriler
        List<String> kozmetik = Arrays.asList(
                "Cilt Bakımı",
                "Saç Bakımı",
                "Makyaj Ürünleri",
                "Parfümler",
                "Erkek Bakım Ürünleri"
        );

// Spor & Outdoor Alt Kategoriler
        List<String> sporOutdoor = Arrays.asList(
                "Spor Giyim",
                "Spor Ayakkabılar",
                "Fitness Ekipmanları",
                "Kamp & Outdoor Malzemeleri",
                "Bisiklet"
        );

// Çocuk & Bebek Alt Kategoriler
        List<String> cocukBebek = Arrays.asList(
                "Bebek Giyim",
                "Çocuk Oyuncakları",
                "Bebek Bakım Ürünleri",
                "Çocuk Mobilyaları",
                "Bebek Arabaları"
        );

// Kitap & Kırtasiye Alt Kategoriler
        List<String> kitapKirtasiye = Arrays.asList(
                "Kitaplar",
                "Kırtasiye Ürünleri",
                "Okul Çantaları",
                "Kalemler & Defterler",
                "Sanat Malzemeleri"
        );

// Evcil Hayvan Alt Kategoriler
        List<String> evcilHayvan = Arrays.asList(
                "Kedi Ürünleri",
                "Köpek Ürünleri",
                "Kuş Ürünleri",
                "Balık Ürünleri",
                "Evcil Hayvan Aksesuarları"
        );

// Oyun & Hobi Alt Kategoriler
        List<String> oyunHobi = Arrays.asList(
                "Video Oyunları",
                "Konsol Aksesuarları",
                "Hobi Setleri",
                "Model Arabalar",
                "Yapbozlar"
        );

// Sağlık & Beslenme Alt Kategoriler
        List<String> saglikBeslenme = Arrays.asList(
                "Vitaminler & Takviyeler",
                "Sporcu Besinleri",
                "Diyet Ürünleri",
                "Sağlık Cihazları",
                "Doğal Ürünler"
        );

        subCategoryService.addSubCategory(moda,2);
        subCategoryService.addSubCategory(elektronik,1);
        subCategoryService.addSubCategory(evYasam,3);
        subCategoryService.addSubCategory(kozmetik,4);
        subCategoryService.addSubCategory(sporOutdoor,5);











    }


}
