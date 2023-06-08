package bll;
//
import bll.validators.EmailValidator;
import bll.validators.Validator;
import dao.ClientDAO;
import model.Client;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientBLL {
    private final List<Validator<Client>> validators;
    private ClientDAO clntDAO;
    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        validators.add(new EmailValidator());
    }

    public Client findById(int id) {
        Client st = ClientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return st;
    }
    public int insertClient(Client client) {
        for (Validator<Client> v : validators) {
            v.validate(client);
        }
        return ClientDAO.insert(client);
    }
}