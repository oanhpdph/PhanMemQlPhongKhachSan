package model;

public class Room {

    private String id;
    private int status;
    private int kindOfRoom;
    private String idPromotion;
    private String code;
    private String roomNumber;
    private String area;
    private String location;
    private String price;

    public Room() {
    }

    public Room(int status, int kindOfRoom, String idPromotion, String code, String roomNumber, String area, String location, String price) {
        this.status = status;
        this.kindOfRoom = kindOfRoom;
        this.idPromotion = idPromotion;
        this.code = code;
        this.roomNumber = roomNumber;
        this.area = area;
        this.location = location;
        this.price = price;
    }

    public Room(String id, int idStatus, int KindOfRoom,String idPromotion, String code, String roomNumber, String area, String location, String price) {
        this.id = id;
        this.status = idStatus;
        this.kindOfRoom = KindOfRoom;
        this.code = code;
        this.roomNumber = roomNumber;
        this.area = area;
        this.location = location;
        this.price = price;
        this.idPromotion=idPromotion;
    }

    public String getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(String idPromotion) {
        this.idPromotion = idPromotion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getKindOfRoom() {
        return kindOfRoom;
    }

    public void setKindOfRoom(int kindOfRoom) {
        this.kindOfRoom = kindOfRoom;
    }

   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
