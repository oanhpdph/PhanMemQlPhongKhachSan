/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.Impl;

import java.util.ArrayList;
import model.item;
import respository.ItemRepos;
import service.ItemService;

/**
 *
 * @author admin
 */
public class ItemImpl implements ItemService {

    ItemRepos ir;

    public ItemImpl() {
        ir = new ItemRepos();
    }

    @Override
    public ArrayList<item> getListI() {
        return ir.getAll();
    }

    @Override
    public void update(item i, String id) {
        ir.update(i, id);
    }

    @Override
    public void insert(item i) {
        ir.insert(i);
    }

    @Override
    public void delete(String id) {
        ir.delete(id);
    }

    @Override
    public ArrayList<item> getSearch(String ma) {
        return ir.getSearch(ma);
    }

}
