package service;

import java.util.List;
import model.Service;
import respository.ServiceRepo;

public class ServiceService implements IService<Service, String> {

    private ServiceRepo repo;

    public ServiceService() {
        repo = new ServiceRepo();
    }

    public List<Service> getByCode(String code) {
        if (code.equals("") || code == null || repo.getIdByCode(code).isEmpty()) {
            return null;
        }
        return repo.getIdByCode(code);

    }

    @Override
    public String insert(Service entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Service entity, String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Service> getAll() {
        return repo.getAll();
    }

    
//    @Override
//    public ArrayList<service> getListDV() {
//        return svr.getAll();
//    }
//
//    @Override
//    public void insert(service s) {
//        svr.insert(s);
//    }
//
//    @Override
//    public void delete(String id) {
//        svr.delete(id);
//    }
//
//    @Override
//    public void update(service s, String id) {
//        svr.update(s, id);
//    }
//
//    @Override
//    public ArrayList<service> getSearch(String ma) {
//        return svr.getSearch(ma);
//    }
//
//    @Override
//    public ArrayList<PromotionSVM> getListPS() {
//        return svr.getAllPS();
//    }
}
