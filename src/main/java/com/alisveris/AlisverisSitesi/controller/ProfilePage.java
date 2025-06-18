package com.alisveris.AlisverisSitesi.controller;

import com.alisveris.AlisverisSitesi.dto.AuctionDTO;
import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Offer;
import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.services.AuctionServices;
import com.alisveris.AlisverisSitesi.services.OfferServices;
import com.alisveris.AlisverisSitesi.services.UserServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfilePage {

    private final UserServices userServices;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuctionServices auctionServices;
    private final OfferServices offerServices;

    public ProfilePage(UserServices userServices, BCryptPasswordEncoder bCryptPasswordEncoder, AuctionServices auctionServices, OfferServices offerServices) {
        this.userServices = userServices;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.auctionServices = auctionServices;
        this.offerServices = offerServices;
    }


    @GetMapping("/profilePage")
    public String profilePage(Model model) throws SQLException, IOException {
        User user = userServices.activeUser();
        List<Offer> offers = offerServices.getFinishedAuctionFromOffer(user);
        List<Offer> offersContinuing = offerServices.getContinuingOffer(user);

        List<AuctionDTO> auctionWithImages =new ArrayList<>();
        List<AuctionDTO> auctionContinuingWithImages = new ArrayList<>();
        List<AuctionDTO> auctionDTOActiveAuction = auctionServices.auctionToAuctiohDTO(auctionServices.getAuctionByUser(user));
        for(Offer offer : offers){
                auctionWithImages.add(auctionServices.auctionDTOList(offer.getAuction()));


        }
        for(Offer offer : offersContinuing){

            auctionContinuingWithImages.add(auctionServices.auctionDTOList(offer.getAuction()));
        }


            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("activeAuctions",auctionDTOActiveAuction);
                model.addAttribute("purchasedProducts",auctionWithImages);
                model.addAttribute("continuingOffer",auctionContinuingWithImages);

            } else {
                // Kullanıcı bulunamadıysa hata mesajı ekleyin
                model.addAttribute("error", "User not found");
                return "login";

            }

        return "profilepage";
    }


    @PostMapping("/profilePage")
    public String update(@ModelAttribute("user") User user, @RequestParam("action") String action, Model model, @RequestParam(value = "password", required = false) String pass1, @RequestParam(value = "passwordRepeat", required = false) String pass2) {
        if ("change".equals(action)) {

            User user1 = userServices.getUser(user.getUsername()).orElse(null);

            if (!user1.getName().equals(user.getName())) user1.setName(user.getName());
            if (!user1.getSurname().equals(user.getSurname())) user1.setSurname(user.getSurname());
            if (!user1.getUserMailAdress().equals(user.getUserMailAdress()))
                user1.setUserMailAdress(user.getUserMailAdress());

            //   model.addAttribute("user",user1);

            userServices.updateUser(user1);
            model.addAttribute("updateMessage", "Profil başarıyla güncellendi!"); // Başarı mesajı ekliyoruz


            return "profilePage";

        } else if ("changePasswordButton".equals(action)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getPrincipal() instanceof UserDetails) {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                User userx = userServices.getUser(username).orElse(null);
                if (userx != null) {
                    if (pass1.equals(pass2)) {
                        userx.setPassword(bCryptPasswordEncoder.encode(pass1));
                        userServices.updateUser(userx);
                        model.addAttribute("user", userx); // User'ı profile sayfasına ekleyin
                        model.addAttribute("passwordMessage", "Şifre Başarıyla Güncellendi");
                        return "profilepage"; // doğru yönlendirme
                    } else {
                        model.addAttribute("passwordMessage", "Şifreler eşleşmiyor!");
                        model.addAttribute("user", userx);
                        return "profilePage"; // hata mesajıyla geri dön
                    }
                }
            }
            return "login";

        }

        return "error";


    }


}
