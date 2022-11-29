/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.PromotionR;

/**
 *
 * @author TGDD
 */
public interface IPromotionR {
    public ArrayList<PromotionR> getAll();
    public PromotionR searchPromotionR(String id, String dateEnd);
}
