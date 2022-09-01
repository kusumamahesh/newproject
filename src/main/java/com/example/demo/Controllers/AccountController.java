package com.example.demo.Controllers;

import com.example.demo.Entities.Shipment;
import com.example.demo.Entities.User;
import com.example.demo.MongoDB.MongoDB;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/account")
public class AccountController {

    private User user = null;

    @GetMapping(path = "login")
    public String viewLoginPage(Model model){
        if(this.user != null)
            return "redirect:/account/dashboard";
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(path = "login")
    public String signInUser(@ModelAttribute User user, Model model){
        if(this.user != null)
            return "redirect:/account/dashboard";
        try{
            MongoDB.authenticateUser(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", new User());
            return "login";
        }
        this.user = user;
        return "redirect:/account/dashboard";
    }

    @GetMapping(path = "dashboard")
    public String viewDashboard(Model model){
        if(this.user == null)
            return "redirect:/account/login";
        else
            model.addAttribute("user", this.user);
            return "dashboard";
    }

    @GetMapping(path = "logout")
    public String logoutUser(){
        if(this.user != null)
            this.user = null;
        return "redirect:/account/login";
    }

    @GetMapping(path="devices")
    public String getDevicesDataStream(Model model){
        if(this.user == null)
            return "redirect:/account/login";
        else{
            model.addAttribute("devices", MongoDB.getDevices());
            return "devices";
        }
    }

    @GetMapping(path = "createShipment")
    public String createShipmentPage(Model model){
        if(this.user == null)
            return "redirect:/account/login";
        else{
            model.addAttribute("shipment", new Shipment());
            model.addAttribute("devices", MongoDB.getDeviceIDs());
            return "createShipment";
        }
    }

    @PostMapping(path = "createShipment")
    public String submitShipmentForm(@ModelAttribute Shipment shipment, Model model){
        if(this.user == null)
            return "redirect:/account/login";
        else{
            model.addAttribute("devices", MongoDB.getDeviceIDs());
            try {
                notEmpty(shipment.getSi_no(), "Enter Shipment Number!");
                notEmpty(shipment.getContainer_no(), "Enter Container Number!");
                notEmpty(shipment.getDesc(), "Provide a description!");
                notEmpty(shipment.getRoute(), "Select a Route!");
                notEmpty(shipment.getGoods(), "Select Goods!");
                notEmpty(shipment.getDevice(), "Select Device!");
                notEmpty(shipment.getDate(), "Enter Date!");
                notEmpty(shipment.getPo_no(), "Enter PO Number!");
                notEmpty(shipment.getDelivery_no(), "Enter Delivery Number!");
                notEmpty(shipment.getNdc_no(), "Enter NDC Number!");
                notEmpty(shipment.getBatch_id(), "Enter Batch ID!");
                notEmpty(shipment.getSerial_no(), "Enter Serial Number!");
                MongoDB.addShipment(this.user.getUsername(), shipment.getSi_no(), shipment.getContainer_no(),
                        shipment.getDesc(), shipment.getRoute(), shipment.getGoods(), shipment.getDevice(), shipment.getDate(), shipment.getPo_no(), shipment.getDelivery_no(),
                        shipment.getNdc_no(), shipment.getBatch_id(), shipment.getSerial_no());
            }
            catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("shipment", shipment);
                return "createShipment";
            }
            model.addAttribute("success", "Submitted successfully!");
            model.addAttribute("shipment", new Shipment());
            return "createShipment";
        }
    }

    private static boolean isAlphabetic(String s) throws Exception {
        for(char c: s.toCharArray()){
            if(!Character.isAlphabetic(c))
                throw new Exception("Only alphabetic characters allowed!");
        }
        return true;
    }

    private static boolean notEmpty(String s, String msg) throws Exception{
        if(s.equals(""))
            throw new Exception(msg);
        return true;
    }

}
