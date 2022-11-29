package service;

import java.util.ArrayList;
import model.Room;

/**
 *
 * @author TGDD
 */
public interface IRoom {

    public void insert(Room room);

    public void delete(String id);

    public void update(String id, Room room);

    public ArrayList<Room> getAll();
    
}
