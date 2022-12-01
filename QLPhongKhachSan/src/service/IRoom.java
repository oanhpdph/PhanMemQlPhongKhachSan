package service;

import java.util.ArrayList;
import model.Room;

/**
 *
 * @author TGDD
 */
public interface IRoom {

    public String insert(Room room);

    public String  delete(String id);

    public String update(String id, Room room);

    public ArrayList<Room> getAll();

    public Room getSearchRoom(String code);

}
