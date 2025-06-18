package com.alisveris.AlisverisSitesi.controller;

import com.alisveris.AlisverisSitesi.dto.AuctionDTO;
import com.alisveris.AlisverisSitesi.models.*;
import com.alisveris.AlisverisSitesi.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private final AdminServices adminServices;
private final ProductServices productServices;
private final AuctionServices auctionServices;
private final RequestServices requestServices;
private final UserServices userServices;
private final OfferServices offerServices;
private final DatabaseService databaseService;
private final CommentServices commentServices;
private final ImageService imageService;
private final ConversationService conversationService;
private final MessageServices messageServices;


    public AdminController(AdminServices adminServices, ProductServices productServices, AuctionServices auctionServices, RequestServices requestServices, UserServices userServices, OfferServices offerServices, DatabaseService databaseService, CommentServices commentServices, ImageService imageService, ConversationService conversationService, MessageServices messageServices) {
        this.adminServices = adminServices;
        this.productServices = productServices;
        this.auctionServices = auctionServices;
        this.requestServices = requestServices;
        this.userServices = userServices;
        this.offerServices = offerServices;
        this.databaseService = databaseService;
        this.commentServices = commentServices;
        this.imageService = imageService;
        this.conversationService = conversationService;
        this.messageServices = messageServices;
    }


    @GetMapping("/adminPanel")
    public String getPanel(Model model) throws SQLException, IOException {
        List<AuctionDTO> auctions =auctionServices.auctionToAuctiohDTO(auctionServices.auctionListbyStatus(Status.Pending));


        if(auctions != null){
            model.addAttribute("pendingAuction",auctions);
        }
        if(auctions.size() == 0){
            model.addAttribute("errorMessage","ÜRÜN İSTEĞİ BULUNMAMAKTADIR");

        }

    return "AdminPanel";
}
@GetMapping("/adminPanel/{productId}")
    public String getProduct(@PathVariable("productId") Integer productId,Model model) throws SQLException, IOException {

        AuctionDTO auctionDTO = auctionServices.auctionDTOList(auctionServices.getAuction(productId));
        model.addAttribute("auctionDTO",auctionDTO);


        return "adminProductPage";


}
    @PostMapping("/adminPanel")
    public String getProduct(@RequestParam("productId") Integer productId,@RequestParam("action") String action,Model model) throws SQLException, IOException {
        Auction auction = auctionServices.getAuction(productId);
        Requests requests = requestServices.findRequestByAuction(auction);
        if(action.equals("confirm")){
            auction.setStatus(Status.Active);
            auctionServices.addNewAction(auction);
            requests.setStatus(Status.Finish);
            requestServices.addRequest(requests);
        }
        if(action.equals("refuse")){
            deleteProductAndRelatedData(productId);

        }

        List<AuctionDTO> auctions =auctionServices.auctionToAuctiohDTO(auctionServices.auctionListbyStatus(Status.Pending));
        model.addAttribute("pendingAuction",auctions);



        return "redirect:/adminPanel";

    }
    @Transactional
    public void deleteProductAndRelatedData(Integer productId) {
        Product product = productServices.getProduct(productId);
        Auction auction = auctionServices.getAuction(productId);

        // Sırasıyla ilişkili tüm verileri silme
        requestServices.deleteRequestsByAuction(auction);
        imageService.deleteImagesByProductId(productServices.getProduct(productId)); // İlk önce resimleri sil
        auctionServices.deleteAuctionByProductId(product); // Auction'ı sonra sil
            productServices.deleteProductByProduct(product); // En son ürünü sil
    }

@PostMapping("/adminPanel/{productId}")
    public String deneme(@RequestParam("button") String buttonValue,@PathVariable("productId") Integer productId){
        String onayla = "confirmButton";
        String reddet = "refuseButton";

    Auction auction = auctionServices.getAuction(productId);
    Requests requests = requestServices.findRequestByAuction(auction);
        if(buttonValue.equals(onayla)){
            auction.setStatus(Status.Active);
            auctionServices.addNewAction(auction);
            requests.setStatus(Status.Finish);
            requestServices.addRequest(requests);

        }
        if(buttonValue.equals(reddet)){
            deleteProductAndRelatedData(productId);
        }
        return "redirect:/adminPanel";

}

@GetMapping("/admin/users")
    public String getAllUsers(Model model){
        List<User> allUsers = userServices.getAllUser();

        model.addAttribute("users",allUsers);

        return "AdminUserPage";

}
    @GetMapping("/admin/products")
    public String getAllProducts(Model model) throws SQLException, IOException {
        List<Auction> allAuction = auctionServices.getAllAuction();
        List<AuctionDTO> auctionDTOS = auctionServices.auctionToAuctiohDTO(allAuction);

        model.addAttribute("auctionDTOS",auctionDTOS);


        return "AdminAllProductPage";

    }

    @GetMapping("/admin/products/{productId}")
    public String productDetail(@PathVariable("productId") Integer productId,Model model){
        List<String> surec = new ArrayList<>();
            Auction auction = auctionServices.getAuction(productId);
        surec.add("Ürün " + auction.getStartingPrice() + "tarihinde listelendi. Başlangıç fiyatı : " +auction.getStartingPrice() );

            List<Offer> offers = offerServices.getOfferListByProductId(productId);

            model.addAttribute("offer",offers);
            model.addAttribute("auction",auction);

        return "productDetailPage";

    }
    public void deleteAllMessages(List<Conversation> conversations){
            for(Conversation con : conversations){
                messageServices.deleteMessageByConversation(con);

            }


    }
    public void deleteAllConversation(List<Conversation> conversations,Integer userId){
        for(Conversation con : conversations){
            conversationService.deleteConversationByUser(userServices.getUser(userId));
        }

    }


    public void deleteUser(Integer userId){

        //comment'in içeriğini siliyoruz cünkü user'ın referans verdiği tablolardaki en bağımlı olan orası.
        deleteAllMessages(conversationService.getConversationByUser(userServices.getUser(userId)));
        deleteAllConversation(conversationService.getConversationByUser(userServices.getUser(userId)),userId);
        requestServices.deleteRequestByUserId(userId);
        commentServices.deleteCommentByUserId(userId);
        deleteProductFromImage(userServices.getUser(userId));
        deleteProductFromAuction(userServices.getUser(userId));
        teklifVerilenAuctionBul(userServices.getUser(userId));
        productServices.deleteProductByUserId(userId);
        offerServices.deleteOfferByUserId(userId);
        userServices.deleteUserByUserId(userId);



    }
    //TEKLİF VERDİĞİ TÜM ÜRÜNLERİ BAŞLANGIÇ FİYATINA ÇEKTİM.
    public void teklifVerilenAuctionBul(User user){
        List<Auction> auctions = auctionServices.findProductsOnOffer(user);
        for(Auction auction : auctions){
            auction.setCurrentPrice(0.0);
        }

    }

    public void deleteProductFromImage(User user){
        List<Product> productList = productServices.getAllProductByUser(user);
        for(Product pr : productList){
            imageService.deleteImagesByProductId(pr);
        }
    }
    public void deleteProductFromAuction(User user){
        List<Product> productList = productServices.getAllProductByUser(user);
        for(Product pr : productList){
            auctionServices.deleteAuctionByProductId(pr);
        }

    }



    @PostMapping("/admin/users")
    public String deleteUsers(@RequestParam("button") String buttonValue,@RequestParam Integer userId){

        if(buttonValue.equals("delete")){
          deleteUser(userId);
            return "redirect:/admin/users";
        }
        return "AdminUserPage";

    }
    @PostMapping("/admin/products/filter")
    public String filter(@RequestParam(value = "activeAuction", required = false) String activeAuction, Model model) throws SQLException, IOException {
                if(activeAuction.equals("true")){
                 List<AuctionDTO> auctionDTOS = auctionServices.auctionToAuctiohDTO(auctionServices.auctionListbyStatus(Status.Active));
                    model.addAttribute("auctionDTOS",auctionDTOS);
                }
                else{
                    model.addAttribute("auctionDTOS",auctionServices.auctionToAuctiohDTO(auctionServices.getAllAuction()));
                }


        return "AdminAllProductPage";


    }


}
