package service;

import java.util.ArrayList;
import model.Room;
import respository.RoomViewrepo;

public class RoomService implements IRoom{

   RoomViewrepo rvr;
    
    public RoomService(){
        this.rvr = new RoomViewrepo();
    }
    @Override
    public void insert(Room room){
        this.rvr.insert(room);
    }
    
    @Override
    public void update(String id,Room room){
        this.rvr.update(id, room);
    }
    @Override
    public void delete(String id){
        this.rvr.delete(id);
    }
    @Override
    public ArrayList<Room> getAll(){
       return this.rvr.getAll();
    }
}
