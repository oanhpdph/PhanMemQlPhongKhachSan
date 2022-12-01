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
    public String insert(Room room) {
        boolean insertRoom=rvr.insert(room);
        if(insertRoom){
            return "Thêm thành công";
        }else{
            return "Thêm thất bại!";
        }
    }

    @Override
    public String update(String id, Room room) {
        boolean updateRoom=rvr.update(id,room);
        if(updateRoom){
            return "Update thành công";
        }else{
            return "Update thất bại!";
        }
    }

    @Override
    public String delete(String id) {
        boolean deleteRoom=rvr.delete(id);
        if(deleteRoom){
            return "Xóa thành công";
        }else{
            return "Xóa thất bại!";
        }
    }

    @Override
    public ArrayList<Room> getAll() {
        return this.rvr.getAll();
    }
    
    @Override
    public Room getSearchRoom(String code){
        return this.rvr.getSearchRoom(code);
    }

}
