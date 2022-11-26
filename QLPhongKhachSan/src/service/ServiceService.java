/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.Service;
import viewModel.PromotionSVM;

/**
 *
 * @author admin
 */
public interface ServiceService {

    public ArrayList<Service> getListDV();
    
    public void insert(Service s);
    
    public void delete(String id);
    
    public void update(Service s, String id);
    
    public ArrayList<Service> getSearch(String ma);
    
    public ArrayList<PromotionSVM> getListPS();
}
