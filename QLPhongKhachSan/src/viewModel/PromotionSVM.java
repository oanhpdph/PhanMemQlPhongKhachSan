/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

import java.util.Date;

/**
 *
 * @author admin
 */
public class PromotionSVM {

    private String id;
    private int value;
    private Date dStart;
    private Date dEnd;

    public PromotionSVM() {
    }

    public PromotionSVM(String id, int value, Date dStart, Date dEnd) {
        this.id = id;
        this.value = value;
        this.dStart = dStart;
        this.dEnd = dEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getdStart() {
        return dStart;
    }

    public void setdStart(Date dStart) {
        this.dStart = dStart;
    }

    public Date getdEnd() {
        return dEnd;
    }

    public void setdEnd(Date dEnd) {
        this.dEnd = dEnd;
    }

}
