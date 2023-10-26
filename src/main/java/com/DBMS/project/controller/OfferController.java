package com.DBMS.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.DBMS.project.model.Offer;
import com.DBMS.project.repository.OfferRepository;

@Controller
@RequestMapping("/offers")
public class OfferController {
    private final OfferRepository offerRepository;
    
    public OfferController(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @GetMapping("/all")
    public String getOffers(Model model) {
        List<Offer> offers = offerRepository.getAll();
        model.addAttribute("offers", offers);
        return "offers";
    }

    @GetMapping("/{id}")
    @Deprecated
    public String getOffer(Model model, @PathVariable Long id) {
        Optional<Offer> offer = offerRepository.getOffer(id);
        if(offer.isPresent()) {
            model.addAttribute("offer", offer.get());
            return "offer";
        }else {
            return "notfound";
        }
    }
}
