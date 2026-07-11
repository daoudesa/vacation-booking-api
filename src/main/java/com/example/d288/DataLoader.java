package com.example.d288;

import com.example.d288.dao.CustomerRepository;
import com.example.d288.dao.DivisionRepository;
import com.example.d288.entities.Customer;
import com.example.d288.entities.Division;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final DivisionRepository divisionRepository;

    public DataLoader(CustomerRepository customerRepository,
                      DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public void run(String... args) {
        // only insert sample data if we don't already have more than the seeded customer
        if (customerRepository.count() > 1) {
            return;
        }

        // reuse an existing division from the seeded data (division_id 4 = California, country_id 1 = U.S.)
        Division division = divisionRepository.findById(4L)
                .orElseThrow(() -> new RuntimeException("Seed division not found — check your database"));

        Date now = new Date();

        Customer c1 = new Customer();
        c1.setFirstName("John");
        c1.setLastName("Smith");
        c1.setAddress("123 Main St");
        c1.setPostalCode("90210");
        c1.setPhone("555-123-4567");
        c1.setDivision(division);
        c1.setCreateDate(now);
        c1.setLastUpdate(now);

        Customer c2 = new Customer();
        c2.setFirstName("Emily");
        c2.setLastName("Johnson");
        c2.setAddress("456 Oak Ave");
        c2.setPostalCode("90211");
        c2.setPhone("555-234-5678");
        c2.setDivision(division);
        c2.setCreateDate(now);
        c2.setLastUpdate(now);

        Customer c3 = new Customer();
        c3.setFirstName("Michael");
        c3.setLastName("Williams");
        c3.setAddress("789 Pine Rd");
        c3.setPostalCode("90212");
        c3.setPhone("555-345-6789");
        c3.setDivision(division);
        c3.setCreateDate(now);
        c3.setLastUpdate(now);

        Customer c4 = new Customer();
        c4.setFirstName("Sarah");
        c4.setLastName("Brown");
        c4.setAddress("321 Cedar Ln");
        c4.setPostalCode("90213");
        c4.setPhone("555-456-7890");
        c4.setDivision(division);
        c4.setCreateDate(now);
        c4.setLastUpdate(now);

        Customer c5 = new Customer();
        c5.setFirstName("David");
        c5.setLastName("Davis");
        c5.setAddress("654 Elm St");
        c5.setPostalCode("90214");
        c5.setPhone("555-567-8901");
        c5.setDivision(division);
        c5.setCreateDate(now);
        c5.setLastUpdate(now);

        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
        customerRepository.save(c4);
        customerRepository.save(c5);
    }
}