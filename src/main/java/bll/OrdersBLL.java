package bll;
import bll.validators.StockValidator;
import bll.validators.Validator;
import dao.OrdersDAO;
import dao.ProductDAO;
import model.Orders;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
public class OrdersBLL {
    private final List<Validator<Orders>> validators;
    private OrdersDAO ordersDAO;
    public OrdersBLL() {
        validators = new ArrayList<Validator<Orders>>();
        validators.add(new StockValidator());
        ordersDAO = new OrdersDAO();
    }

    public Orders findById(int id) {
        Orders ord = OrdersDAO.findById(id);
        if (ord == null) {
            throw new NoSuchElementException("Order id = " + id + " not found!");
        }
        return ord;
    }

    public int insert(Orders orders) {
        ProductBLL prod = new ProductBLL();
        for (Validator<Orders> v : validators) {
            v.validate(orders);
        }
        Product p = prod.findById(orders.getProd_id());
        p.setQuantity(p.getQuantity()-orders.getQuantity());
        return OrdersDAO.insert(orders);
    }
}
