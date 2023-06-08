package bll;

import bll.validators.EmailValidator;
import bll.validators.QuantityValidator;
import bll.validators.Validator;
import dao.ProductDAO;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL {
    private final List<Validator<Product>> validators;

    public ProductBLL() {
        validators = new ArrayList<Validator<Product>>();
        validators.add(new QuantityValidator());
    }

    public Product findById(int id) {
        Product st = ProductDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return st;
    }
    public int insertProduct(Product product) {
        for (Validator<Product> v : validators) {
            v.validate(product);
        }
        return ProductDAO.insert(product);
    }
}
