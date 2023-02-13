package ru.pizzaneo.products.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.pizzaneo.models.dto.MenuGroupsDto;
import ru.pizzaneo.models.dto.MenuMatrixDto;
import ru.pizzaneo.products.domain.CurrentProductRow;
import ru.pizzaneo.products.service.ProductService;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/groups")
    public MenuGroupsDto getGroups() {
        return productService.getMenuGroups().toDto();
    }

    @GetMapping("/groups/{groupId}/{page}/{size}")
    public MenuMatrixDto getProducts(
            @PathVariable("groupId") long groupId
            , @PathVariable("page") int page
            , @PathVariable("size") int size) {
        return productService.getMenuMatrix(groupId, page, size).toDto();
    }

    @GetMapping("/products")
    public MenuMatrixDto getProducts() {
        return productService.getMenuMatrix(0, 0, 0).toDto();
    }

    @GetMapping("/products/{variationIdUuid}")
    public MenuMatrixDto.MenuMatrixRowDto getProduct(@PathVariable("variationIdUuid") String productId) {
        return productService.getCurrentProductInfo(productId).toDto();
    }

    @GetMapping("/products/{variationIdUuids}/all")
    public MenuMatrixDto getProducts(@PathVariable("variationIdUuids") List<String> productId) {
        MenuMatrixDto menuMatrixDto = CurrentProductRow.toDto(productService.getProductsInfo(productId));
        return menuMatrixDto;
    }
}
