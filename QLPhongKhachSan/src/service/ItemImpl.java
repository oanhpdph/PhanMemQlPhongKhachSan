/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.Impl;

import java.util.ArrayList;
import java.util.List;
import model.Item;
import respository.ItemRepos;
import service.IService;

/**
 *
 * @author admin
 */
public class ItemImpl implements IService<Item, String> {

    ItemRepos ir;

    public ItemImpl() {
        ir = new ItemRepos();
    }

//    @Override
//    public ArrayList<item> getListI() {
//        return ir.getAll();
//    }
//
//    @Override
//    public void update(item i, String id) {
//        ir.update(i, id);
//    }
//
//    @Override
//    public void insert(item i) {
//        ir.insert(i);
//    }
//
//    @Override
//    public void delete(String id) {
//        ir.delete(id);
//    }
//
    public ArrayList<Item> getSearch(String ma) {
        return ir.getSearch(ma);
    }

    @Override
    public String insert(Item i) {
        ir.insert(i);
        return "Thành công";
    }

    @Override
    public void update(Item i, String id) {
        ir.update(i, id);
    }

    @Override
    public void delete(String id) {
        ir.delete(id);
    }

    @Override
    public List<Item> getAll() {
        return ir.getAll();
    }

}
