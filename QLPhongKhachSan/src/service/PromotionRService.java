package service;

import java.util.ArrayList;
import model.PromotionR;
import respository.PromotionRRepo;

public class PromotionRService implements IPromotionR{

    private PromotionRRepo repo;

    public PromotionRService() {
        repo = new PromotionRRepo();
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
