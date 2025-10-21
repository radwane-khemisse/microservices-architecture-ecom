package org.sid.billingsevice.web;

import org.sid.billingsevice.entities.Bill;
import org.sid.billingsevice.feign.CustomerRestClient;
import org.sid.billingsevice.feign.ProductRestClient;
import org.sid.billingsevice.repositories.BillRepository;
import org.sid.billingsevice.repositories.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private CustomerRestClient customerRestClient;
    @Autowired
    private ProductRestClient  productRestClient;
    @GetMapping("/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill bill=billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
           productItem.setProduct(productRestClient.getProductById(productItem.getProductid()));
        });
        return bill;
    }
}
