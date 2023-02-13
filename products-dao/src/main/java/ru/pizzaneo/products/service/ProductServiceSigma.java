package ru.pizzaneo.products.service;

import org.springframework.stereotype.Service;
import ru.pizzaneo.products.dao.ProductDao;
import ru.pizzaneo.products.domain.CurrentProductRow;
import ru.pizzaneo.products.domain.MenuGroups;
import ru.pizzaneo.products.domain.MenuMatrix;

import java.util.List;

@Service
public class ProductServiceSigma implements ProductService {

    private final ProductDao productDao;
    private final TicketService ticketService;

    public ProductServiceSigma(ProductDao productDao, TicketService ticketService) {
        this.productDao = productDao;
        this.ticketService = ticketService;
    }

    @Override
    public MenuGroups getMenuGroups() {
        String ticket = this.ticketService.getTicket();
        return this.productDao.requestMenuGroups(ticket);
    }

    @Override
    public MenuMatrix getMenuMatrix(long groupId, int page, int size) {
        String ticket = this.ticketService.getTicket();
        return productDao.requestMenuMatrix(ticket, groupId, page, size);
    }

    @Override
    public CurrentProductRow getCurrentProductInfo(String productId) {
        String ticket = this.ticketService.getTicket();
        return productDao.requestProductRow(ticket, productId);
    }

    @Override
    public List<CurrentProductRow> getProductsInfo(List<String> productIds) {
        String ticket = this.ticketService.getTicket();
        return productDao.requestProductsRows(ticket, productIds);
    }
}
