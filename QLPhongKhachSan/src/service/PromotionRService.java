package service;

import java.util.ArrayList;
import model.PromotionR;
import respository.PromotionRrepo;

public class PromotionRService implements IPromotionR{

    private PromotionRrepo repo;

    public PromotionRService() {
        repo = new PromotionRrepo();
    }

    @Override
    public PromotionR searchPromotionR(String id, String dateEnd) {
        return this.repo.searchPromotionR(id,dateEnd);
    }
    @Override
   public ArrayList<PromotionR> getAll(){
       return this.repo.getAll();
   }
}
