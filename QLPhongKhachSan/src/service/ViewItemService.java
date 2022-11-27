/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Service;

import model.Item;
import viewModel.ViewItem;
import java.util.ArrayList;
import java.util.List;
import respository.ViewItemRepo;
import service.IService;

/**
 *
 * @author Admin
 */
public class ViewItemService {

    private ViewItemRepo repo;

    public ViewItemService() {
        repo = new ViewItemRepo();
    }

    public List<ViewItem> getAll(String idRoom) {
        return repo.getAll(idRoom);
    }
}
