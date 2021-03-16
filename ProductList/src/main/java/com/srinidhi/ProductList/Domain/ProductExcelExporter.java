package com.srinidhi.ProductList.Domain;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductExcelExporter {
    public ProductExcelExporter(List<Product> productList) {
        this.productList = productList;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("products");
    }

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private List<Product> productList;

    private void writeHeaderRow(){

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Product ID");

        cell = row.createCell(1);
        cell.setCellValue("Product Name");

        cell = row.createCell(2);
        cell.setCellValue("Description");

        cell = row.createCell(3);
        cell.setCellValue("Price");

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
    }
    private void writeDataRows(){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for(Product prod : productList){
            Row row = sheet.createRow(rowCount++);

            Cell cell = row.createCell(0);
            cell.setCellValue(prod.getId());

            cell = row.createCell(1);
            cell.setCellValue(prod.getProductname());

            cell = row.createCell(2);
            cell.setCellValue(prod.getDescription());

            cell = row.createCell(3);
            cell.setCellValue(prod.getPrice());

        }
    }
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRows();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }


}
