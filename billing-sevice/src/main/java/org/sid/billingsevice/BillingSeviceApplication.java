package org.sid.billingsevice;

import org.sid.billingsevice.entities.Bill;
import org.sid.billingsevice.entities.ProductItem;
import org.sid.billingsevice.feign.CustomerRestClient;
import org.sid.billingsevice.feign.ProductRestClient;
import org.sid.billingsevice.model.Customer;
import org.sid.billingsevice.model.Product;
import org.sid.billingsevice.repositories.BillRepository;
import org.sid.billingsevice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;

@SpringBootApplication
@EnableFeignClients
public class BillingSeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingSeviceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BillRepository billRepository,
                                        ProductItemRepository productItemRepository,
                                        CustomerRestClient customerRestClient,
                                        ProductRestClient productRestClient){

        return args -> {

            Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
            Collection<Product> products = productRestClient.getAllProducts().getContent();

            customers.forEach(customer -> {
                Bill bill= new Bill();
                bill.setCustomerId(customer.getId());
                bill.setBillingDate(new Date());

                billRepository.save(bill);

                products.forEach(product -> {
                    ProductItem productItem= ProductItem.builder()
                            .bill(bill)
                            .productid(product.getId())
                            .quantity((int)(1+Math.random()*10))
                            .unitPrice(product.getPrice())
                            .build();
                    productItemRepository.save(productItem);
                });

            });
        };

    }

}
