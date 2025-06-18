package com.alisveris.AlisverisSitesi.controller;

import com.alisveris.AlisverisSitesi.dto.*;
import com.alisveris.AlisverisSitesi.models.*;
import com.alisveris.AlisverisSitesi.services.*;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Home {
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final UserServices userServices;
    private final AuctionServices auctionServices;
    List<SubCategory> subCategoryList;
    List<Category> categoryList;
    private final ProductServices productServices;
    private final OfferServices offerServices;
    private final RequestServices requestServices;
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/images";

    private final CommentServices commentServices;

    private final ImageService imageService;

    public Home(CategoryService categoryService, SubCategoryService subCategoryService, UserServices userServices, AuctionServices auctionServices, ProductServices productServices, OfferServices offerServices, RequestServices requestServices, CommentServices commentServices, ImageService imageService) {
        this.categoryService = categoryService;

        this.subCategoryService = subCategoryService;
        this.userServices = userServices;
        this.auctionServices = auctionServices;
        this.productServices = productServices;
        categoryList = categoryService.getAllCategories();
        subCategoryList = subCategoryService.getSubCategory();

        this.offerServices = offerServices;
        this.requestServices = requestServices;
        this.commentServices = commentServices;


        this.imageService = imageService;
    }


    @GetMapping("/addProduct")
    public String Product(@RequestParam(value = "categoryId", required = false) Integer categoryId, Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("auction", new Auction());


        //List<Category> categoryList = categoryService.getAllCategories();

        model.addAttribute("categories", categoryList);

        model.addAttribute("subCategories", subCategoryList);
        //  List<SubCategory> subCategoryList = subCategoryService.getSubCategory();

        User newUser = userServices.activeUser();
        model.addAttribute("user", newUser);

        return "addProduct";


    }

    @GetMapping("/getSubCategories")
    @ResponseBody
    public List<SubCategory> getSubCategories(@RequestParam("categoryId") Integer categoryId) {

        List<SubCategory> subCategories = subCategoryService.getSubCategoryByCategoryId(categoryId);
        return subCategories;
    }


    @Transactional
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product,
                             @ModelAttribute("category") Category category,
                             @ModelAttribute("subCategory") SubCategory subCategory,
                             @ModelAttribute("auction") Auction auction, Model model, @RequestParam("image") MultipartFile imageFile) throws IOException, SQLException {
        User user = userServices.activeUser();


        CreateProductRequest productRequest = CreateProductRequest
                .builder()
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDescripton(product.getProductDescripton())
                .user(user)
                .subCategory(subCategory)
                .category(category)
                .build();

        byte[] bytes = imageFile.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);


        LocalDateTime finishTime = auction.getFinishTime();
        LocalDateTime now = LocalDateTime.now();
        model.addAttribute("categories", categoryList);
        model.addAttribute("subCategories", subCategoryList);
        // Geçmiş bir tarih kontrolü

        if (finishTime.isBefore(now)) {
            model.addAttribute("error", "Zaman Geçmiş olamaz");
            model.addAttribute("user", user);

            model.addAttribute("categories", categoryList);
            model.addAttribute("subCategories", subCategoryList);
            return "addProduct"; // Hata sayfasına yönlendirin
        }


        long daysBetween = ChronoUnit.DAYS.between(now, finishTime);
        if (daysBetween > 4) {
            model.addAttribute("error", "Bitiş zamanı 4 Günden fazla olamaz");
            model.addAttribute("user", user);
            model.addAttribute("categories", categoryList);

            model.addAttribute("subCategories", subCategoryList);
            return "addProduct"; // Hata sayfasına yönlendirin
        }

        Product pr = productServices.addProduct(productRequest);

        CreateAuctionReques createAuctionReques = CreateAuctionReques.builder()
                .startingPrice(product.getProductPrice())
                .startingTime(LocalDateTime.now())
                .finishTime(auction.getFinishTime())
                .currentPrice(0.0)
                .status(Status.Pending)
                .product(pr)
                .accrual(0.0)
                .boxMessage("")
                .showBox(false)
                .build();

        Images image = new Images().builder()
                .image(blob)
                .product(pr)
                .build();
        imageService.create(image);


        auctionServices.addAuction(createAuctionReques);
        model.addAttribute("user", user);
        return "profilepage";
    }


    @GetMapping("/auction")
    public String homePage(Model model) throws SQLException, IOException {

        auctionServices.suresiBitmisUrunleriCek();

        List<AuctionDTO> auctionsWithImages = new ArrayList<>();
       // List<Auction> activeAuctions = auctionServices.auctionListbyStatus(Status.Active);
        List<Auction> activeAuctions = auctionServices.getAuctionByStatusAndUser(userServices.activeUser(),Status.Active);


        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        for (Auction auction : activeAuctions) {
            auction.setKalanSure(auctionServices.kalanSureHesapla(auction));
            auctionServices.enYuksekTeklifKontrol(auction);
            auctionsWithImages.add(auctionServices.auctionDTOList(auction));
        }


        model.addAttribute("activeAuctions", auctionsWithImages);

        return "homePage";
    }


    /*
        @GetMapping("/home/{productId}")
        public String productPage(@PathVariable("productId") Integer productId,Model model){

            Product product = productServices.getProduct(productId);
            model.addAttribute("product",product);

            return "productDetails";
        }


     */
    @GetMapping("/images")
    public ResponseEntity<byte[]> displayImage(Integer id) throws IOException, SQLException {
        Images image = imageService.viewById(1);
        byte[] imageBytes = null;
        imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
    }

    public List<Auction> getSimilarAuction(Product product){
        Integer subCategory = product.getSubCategory().getSubCategoryId();

        List<Auction> auctions = auctionServices.getAuctionBySubCategory(subCategory,auctionServices.getAuction(product.getProductId()),userServices.activeUser());
        if(auctions.size()>5){
            List<Auction> newAuction = new ArrayList<>();

            for(int i = 0 ; i<auctions.size();i++){
                int max = auctions.size()-1;
                int rastgeleTamSayi = (int) (Math.random() * max) ;
                newAuction.add(auctions.get(rastgeleTamSayi));
                auctions.remove(rastgeleTamSayi);

            }

            return newAuction;
        }
return auctions;


    }

    @GetMapping("/auction/{productId}")
    public String productPage(@PathVariable("productId") Integer productId, Model model) throws SQLException, IOException {
        Product product = productServices.getProduct(productId);

        List<AuctionDTO> auctionDTO = new ArrayList<>();

        model.addAttribute("offer", new Offer());
        Auction auction = auctionServices.getAutionByProduct(productId);
        if (auctionServices.kalanSureHesapla(auction) == -1) {
            Offer offer = offerServices.deneme(auction);
            if (offer != null) {
                model.addAttribute("kazananKisi", offer.getProduct().getUser().getUsername());
                System.out.println(offer.getProduct().getUser().getUsername());
            } else {
                model.addAttribute("kazananKisi", "Bu ürüne teklif gelmedi.");

            }
            model.addAttribute("bidDisabled", true);  // Eğer buton devre dışı bırakılacaksa


        } else {
            model.addAttribute("bidDisabled", false);  // Eğer buton devre dışı bırakılacaksa

        }
        if (auction.getCurrentPrice() == 0) {
            model.addAttribute("txt", "Henüz Teklif verilmedi");

        } else {
            model.addAttribute("txt", "Güncel Teklif " + auction.getCurrentPrice() + "TL");
        }

        if (product != null) {


            model.addAttribute("auction", auction);
            List<AuctionDTO> similarAuction = auctionServices.auctionToAuctiohDTO(getSimilarAuction(product));
            model.addAttribute("similarAuctions",similarAuction);

            AuctionDTO auctionDTO1 = new AuctionDTO(auction);
            model.addAttribute("auctionDto", auctionDTO1);
            byte[] bytes = imageService.displayImage(productId);
            String s = auctionServices.encodeImageToBase64(bytes);
            auctionDTO1.setBase64Image(s);

            List<Comment> comments = commentServices.getCommentByAuction(auction);
            if (comments.size() == 0) {
                model.addAttribute("commentError", "HENÜZ YORUM YOK");

            } else {
                model.addAttribute("comments", comments);

            }

        }
        auction.setKalanSure(auctionServices.kalanSureHesapla(auction));
        model.addAttribute("loggedInUserId",userServices.activeUser());

        return "productPage";

    }


    @PostMapping("/auction/{productId}")
    public String teklifVer(@ModelAttribute("offer") Offer offer, @PathVariable("productId") Integer productId, Model model) throws SQLException, IOException {
        Double para = offer.getAmountOffer();

        Auction auction = auctionServices.getAuction(productId);
        Product product = productServices.getProduct(productId);
        model.addAttribute("bidDisabled", true);
        if (auction == null) {
            System.out.println("Auction bulunamadı");
        }
        if (product == null) {
            System.out.println("product Bulunamadı");
        }
        boolean karsilastirma = product.getUser().equals(userServices.activeUser());

        if (auctionServices.kalanSureHesapla(auction) == -1) {
            model.addAttribute("error", "SÜRE DOLDU ÜRÜN SAYFASINA YÖNLENDİRİLİYORSUNUZ.");
            List<Auction> auctions = auctionServices.auctionListbyStatus(Status.Active);
            AuctionDTO auctionDTO = auctionServices.auctionDTOList(auction);
            model.addAttribute("auctionDto", auctionDTO);
            auction.setKalanSure(auctionServices.kalanSureHesaplaAuctionDto(auctionDTO));
            return "homePage";
        }
        if (karsilastirma) {
            model.addAttribute("error", "Kendi Ürününüze teklif veremezsiniz.");
            AuctionDTO auctionDTO = auctionServices.auctionDTOList(auction);
            model.addAttribute("auctionDto", auctionDTO);
            auction.setKalanSure(auctionServices.kalanSureHesaplaAuctionDto(auctionDTO));
            return "productPage";
        }
        if (para <= auction.getStartingPrice()) {
            model.addAttribute("error", "Başlangıç Teklifinden yüksek bir miktar girmelisiniz!");
            AuctionDTO auctionDTO = auctionServices.auctionDTOList(auction);
            model.addAttribute("auctionDto", auctionDTO);
            auction.setKalanSure(auctionServices.kalanSureHesaplaAuctionDto(auctionDTO));
            return "productPage";
        }

        if (userServices.activeUser().getBalance() < para) {
            model.addAttribute("error", "Yetersiz Bakiye");
            AuctionDTO auctionDTO = auctionServices.auctionDTOList(auction);
            model.addAttribute("auctionDto", auctionDTO);
            auction.setKalanSure(auctionServices.kalanSureHesaplaAuctionDto(auctionDTO));
            return "productPage";

        }


        if (para <= auction.getCurrentPrice()) {
            model.addAttribute("error", "Güncel tekliften daha yukarıda bir teklif verebilirsiniz");
            AuctionDTO auctionDTO = auctionServices.auctionDTOList(auction);
            model.addAttribute("auctionDto", auctionDTO);
            auction.setKalanSure(auctionServices.kalanSureHesaplaAuctionDto(auctionDTO));
            return "productPage";

        }
        CreateOfferRequest createOfferRequest = CreateOfferRequest.builder()
                .amountOffer(offer.getAmountOffer())
                .amountOfferTime(LocalDateTime.now())
                .user(userServices.activeUser())
                .product(product)
                .auction(auction)
                .status(Status.Active)
                .build();
        offerServices.OncekiKullanicininParasiniVer(auction);
        offerServices.addOffer(createOfferRequest);
        auction.setCurrentPrice(para);
        auctionServices.addNewAction(auction);
        userServices.activeUser().setBalance(userServices.activeUser().getBalance() - para);
        userServices.updateUser(userServices.activeUser());
        long sure = auctionServices.kalanSureHesapla(auction);
        if (sure != -1 && sure != 0 && sure < 30) {
            LocalDateTime time = LocalDateTime.now().plusSeconds(30);
            auction.setFinishTime(time);
            auctionServices.addNewAction(auction);

        }


        List<AuctionDTO> auctionDTOS = new ArrayList<>();
        List<Auction> auctions = auctionServices.auctionListbyStatus(Status.Active);
        for (Auction auction1 : auctions) {
            AuctionDTO auctionDTO = auctionServices.auctionDTOList(auction1);
            auctionDTOS.add(auctionDTO);
            auctionServices.kalanSureHesaplaAuctionDto(auctionDTO);
        }
        model.addAttribute("activeAuctions", auctionDTOS);

        return "redirect:/auction";
    }


    public static <T> List<T> mergeListsWithoutDuplicates(List<T> list1, List<T> list2) {
        // Set kullanarak tekrar eden öğeleri engelliyoruz
        Set<T> mergedSet = new HashSet<>(list1);
        mergedSet.addAll(list2);

        // Set'i yeniden List'e çeviriyoruz
        return mergedSet.stream().collect(Collectors.toList());
    }

    @PostMapping("/filter")
    public String filter(@RequestParam(value = "brands", required = false) List<String> category,
                         @RequestParam(value = "priceMin", required = false) Double priceMin,
                         @RequestParam(value = "priceMax", required = false) Double priceMax,
                         @RequestParam(value = "dateFilter", required = false) LocalDate endTime, Model model,
                         @RequestParam(value = "myProduct", required = false) String myProduct
    ) throws SQLException, IOException {

        List<Auction> auctions;
        List<Auction> deneme;


        if (endTime != null) {

            LocalDateTime time = endTime.atStartOfDay();

            if (myProduct != null && myProduct.equals("true")) {
                auctions = mergeListsWithoutDuplicates(auctionServices.filterAuctionList(category, time, priceMin, priceMax, userServices.activeUser()), offerServices.getList(userServices.activeUser()));

            } else {
                auctions = auctionServices.filterAuctionList(category, time, priceMin, priceMax, null);

            }

            //   ad = mergeListsWithoutDuplicates(auctions,offerServices.getList(ae));


        } else {
            if (myProduct != null && myProduct.equals("true")) {
                auctions = mergeListsWithoutDuplicates(auctionServices.filterAuctionList(category, null, priceMin, priceMax, userServices.activeUser()), offerServices.getList(userServices.activeUser()));

            } else {
                auctions = auctionServices.filterAuctionList(category, null, priceMin, priceMax, null);
            }

            // ad = mergeListsWithoutDuplicates(auctions,offerServices.getList(ae));
        }


        List<AuctionDTO> auctionDTOS = new ArrayList<>();
        for (Auction a : auctions) {
            auctionServices.enYuksekTeklifKontrol(a);
        }
        for (Auction auction : auctions) {
            auction.setKalanSure(auctionServices.kalanSureHesapla(auction));


        }
        auctionDTOS = auctionServices.auctionToAuctiohDTO(auctions);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("activeAuctions", auctionDTOS);

        return "homePage";
    }


    @PostMapping("/auction/{productId}/addComment")
    public String addComment(@ModelAttribute("comment") Comment comment, @PathVariable("productId") Integer productId, Model model) throws SQLException, IOException {


        List<Comment> getCommentByAuction = commentServices.getCommentByAuction(auctionServices.getAutionByProduct(productId));

        model.addAttribute("comments", getCommentByAuction);

        CreateCommentRequest request = CreateCommentRequest.builder()
                .user(userServices.activeUser())
                .commentTime(LocalDateTime.now())
                .auction(auctionServices.getAuction(productId))
                .commentText(comment.getCommentText())
                .parentCommentId(comment.getParentCommentId())
                .build();

        commentServices.addComment(request);

        Offer offer = new Offer();
        model.addAttribute("offer", offer);
        AuctionDTO auctionDTO = auctionServices.auctionDTOList(auctionServices.getAuction(productId));
        model.addAttribute("auctionDto", auctionDTO);
        auctionServices.getAuction(productId).setKalanSure(auctionServices.kalanSureHesaplaAuctionDto(auctionDTO));
        model.addAttribute("bidDisabled", false);
        List<Comment> comments = commentServices.getCommentByAuction(auctionServices.getAuction(productId));
        model.addAttribute("comments", comments);

        return "redirect:/auction/{productId}";

    }

}




