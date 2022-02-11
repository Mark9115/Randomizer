package mark.entity;

import mark.service.StudentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static mark.controller.FileUploadController.pathFromController;

public class ParseXLSX {
    private final StudentService studentService;

    public ParseXLSX(StudentService studentService) {
        this.studentService = studentService;
    }

    public void parse() throws IOException {
        HashMap<Integer, String[]> groups = new HashMap<>();
        String path;
        if(!pathFromController.isEmpty()){
            path = new File(pathFromController).getAbsolutePath();
        }else{
            path = new File("src/main/resources/xlsx/test.xlsx").getAbsolutePath();
        }

        FileInputStream inputStream = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);

        int rowStart = Math.min(0, sheet.getFirstRowNum());
        int rowEnd = Math.max(0, sheet.getLastRowNum());

        Cell lastGroup = null;
        for (int rowNum = rowStart + 1; rowNum <= rowEnd; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null)
                continue;


            Cell cellId = row.getCell(0);
            Cell cellName = row.getCell(1);
            Cell cellGroup = row.getCell(2);

            if (cellGroup != null) {
                lastGroup = cellGroup;
            } else {
                cellGroup = lastGroup;
            }

            String cv = String.valueOf(cellGroup.getNumericCellValue());
            String[] array = new String[]{cv, cellName.getStringCellValue()};
            groups.put((int)cellId.getNumericCellValue(), array);
        }

        for(Map.Entry<Integer, String[]> map : groups.entrySet()){
            studentService.addStudent(new Student(
                    map.getKey(),
                    map.getValue()[1],
                    (int)Double.parseDouble(map.getValue()[0])
                    )
            );
        }
    }
}
