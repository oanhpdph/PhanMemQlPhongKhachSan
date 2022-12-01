/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.Room;
import respository.RoomViewrepo;

/**
 *
 * @author FPTSHOP
 */
public class roomServiceManh implements IRoom {

    RoomViewrepo rvr;

    public roomServiceManh() {
        this.rvr = new RoomViewrepo();
    }

    @Override
    public void insert(Room room) {
        this.rvr.insert(room);
    }

    @Override
    public void update(String id, Room room) {
        this.rvr.update(id, room);
    }

    @Override
    public void delete(String id) {
        this.rvr.delete(id);
    }

    @Override
    public ArrayList<Room> getAll() {
        return this.rvr.getAll();
    }

}
