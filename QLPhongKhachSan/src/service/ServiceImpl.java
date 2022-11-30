/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.Impl;

import java.util.ArrayList;
import model.service;
import respository.ServiceRepos;
import service.ServiceService;
import viewModel.PromotionSVM;

/**
 *
 * @author admin
 */
public class ServiceImpl implements ServiceService {

    ServiceRepos svr;

    public ServiceImpl() {
        svr = new ServiceRepos();
    }

    @Override
    public ArrayList<service> getListDV() {
        return svr.getAll();
    }

    @Override
    public void insert(service s) {
        svr.insert(s);
    }

    @Override
    public void delete(String id) {
        svr.delete(id);
    }

    @Override
    public void update(service s, String id) {
        svr.update(s, id);
    }

    @Override
    public ArrayList<service> getSearch(String ma) {
        return svr.getSearch(ma);
    }

    @Override
    public ArrayList<PromotionSVM> getListPS() {
        return svr.getAllPS();
    }
    
    
    
    
    
    
    

}
