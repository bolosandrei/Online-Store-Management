package bll.validators;
import model.Orders;
import model.Product;

import static dao.ProductDAO.findById;

public class StockValidator implements Validator<Orders>{
    public void validate(Orders t){
        Product p = findById(t.getProd_id());
        if(t.getQuantity()<p.getQuantity())
            throw new IllegalArgumentException("Not enough Stock!");
    }
}
