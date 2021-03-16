package com.srinidhi.ProductList.Controller;

import com.srinidhi.ProductList.Domain.Product;
import com.srinidhi.ProductList.Domain.ProductExcelExporter;
import com.srinidhi.ProductList.ExcelReader.ExcelRead;
import com.srinidhi.ProductList.Message.ResponseMessage;
import com.srinidhi.ProductList.Service.ProductService;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
//import sun.lwawt.macosx.CSystemTray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
public class ProductController {

    @Autowired
    private ProductService service;


    @GetMapping("/")
    public String viewHomePage(Model model, HttpServletRequest req) {
        List<Product> listproduct = service.listAll();
        model.addAttribute("listproduct", listproduct);

        Locale currloc = req.getLocale();
        String CountryCode = currloc.getCountry();
        String CountryName = currloc.getDisplayCountry();


        String LangCode = currloc.getLanguage();
        String LangName = currloc.getDisplayLanguage();

        //System.out.println(CountryCode +"   "+ CountryName);
        //System.out.println(LangCode + "  "+ LangName);
        return "index";
    }

    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("product", new Product());
        return "new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute("product") Product pd) {
        service.save(pd);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("new");
        Product pd = service.get(id);
        mav.addObject("product", pd);
        return mav;

    }
    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        service.delete(id);
        return "redirect:/";
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");


        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=gat_productlist.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Product> listProducts = service.listAll();

       ProductExcelExporter excelExporter = new ProductExcelExporter(listProducts);

        excelExporter.export(response);
    }


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelRead.hasExcelFormat(file)) {
            try {
                service.savefile(file);
                System.out.println("file saved check");
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                System.out.println("file not saved check");
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

}
