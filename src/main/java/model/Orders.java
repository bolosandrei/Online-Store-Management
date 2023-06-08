package model;

public class Orders {
    private int id;
    private int prod_id;
    private int client_id;
    private int quantity;

    public Orders() {//constructor gol
    }

    public Orders(int id, int prod_id, int client_id, int quantity) {//constructor cu ID
        this.id = id;
        this.prod_id = prod_id;
        this.client_id = client_id;
        this.quantity = quantity;
    }

    public Orders(int prod_id, int client_id, int quantity) {//constructor fara ID
        this.prod_id = prod_id;
        this.client_id = client_id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProd_id() {
        return prod_id;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", prod_id=" + prod_id +
                ", client_id=" + client_id +
                ", quantity=" + quantity +
                '}';
    }
}
