package com.srinidhi.ProductList.Service;

import com.srinidhi.ProductList.Domain.Product;
import com.srinidhi.ProductList.ExcelReader.ExcelRead;
import com.srinidhi.ProductList.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> listAll() {
        return repo.findAll();
    }

    public void save(Product pd) {
        repo.save(pd);
    }

    public Product get(long id) {
        return repo.findById(id).get();
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public void savefile(MultipartFile file) {
        try {
            System.out.println("comming to savefile path");
            List<Product> products = ExcelRead.excelToProducts(file.getInputStream());
            repo.saveAll(products);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }
}
