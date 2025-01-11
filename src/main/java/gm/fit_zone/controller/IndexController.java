package gm.fit_zone.controller;

import gm.fit_zone.Service.IClientService;
import gm.fit_zone.model.Client;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ViewScoped
public class IndexController {
    @Autowired
    IClientService clientService;

    private List<Client> clients;
    private Client selectedClient;
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        this.clients = this.clientService.listClients();
        // this.clients.forEach(client -> logger.info("Client: {}", client.toString()));
    }

    public void addClient() {
        this.selectedClient = new Client();
    }

    public void saveClient() {
        logger.info("Client to add: " + this.selectedClient);
        // ADD NEW CLIENT
        if (this.selectedClient.getId() == null) {
            this.clientService.saveClient(this.selectedClient);
            this.clients.add(this.selectedClient);
            FacesContext
                    .getCurrentInstance()
                    .addMessage(null, new FacesMessage("Client added"));
        }
        // UPDATE CLIENT
        else {
            this.clientService.saveClient(this.selectedClient);
            FacesContext
                    .getCurrentInstance()
                    .addMessage(null, new FacesMessage("Client updated"));
        }
        // Hide modal
        PrimeFaces.current().executeScript("PF('windowModalClient').hide()");
        // Update table with ajax
        PrimeFaces.current().ajax().update("clients-form:messages", "clients-form:clients-table");
        // Reset Object: selectedClient
        this.selectedClient = null;
    }

    public void deleteClient() {
        logger.info("Client to delete: " + this.selectedClient);
        this.clientService.deleteClientById(this.selectedClient);
        // Remove record from clients list
        this.clients.remove(this.selectedClient);
        // Reset selectedClient
        this.selectedClient = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Client deleted"));
        // Reload
        PrimeFaces.current().ajax().update("clients-form:messages", "clients-form:clients-table");
    }
}
