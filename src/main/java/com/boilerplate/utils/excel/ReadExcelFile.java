package com.boilerplate.utils.excel;

import java.io.*;
import java.util.*;

/*import org.apache.poi.EmptyFileException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boilerplate.modules.user.domain.User;
import com.boilerplate.modules.user.service.UserService;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReadExcelFile {
    private final Logger logger = LoggerFactory.getLogger(ReadExcelFile.class);

    /*@Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    private final DataFormatter formatter = new DataFormatter();

    public String getFieldByIndex(Row row, int i){
        return formatter.formatCellValue(row.getCell(i)).trim();
    }

    @Transactional
    public ImportUsersResponse addUsersByBatch(String file, Long projectId)
            throws EmptyFileException, IOException, RuntimeException {
        System.out.println("ImportUsersResponse--------------------------------------");
        List<String> errors = new ArrayList<>();
        ImportUsersResponse response = new ImportUsersResponse();
        DataFormatter formatter = new DataFormatter();
        XSSFWorkbook inputWorkbook = getWorkbookFile(file);
        try {
            XSSFSheet sheetTrans = inputWorkbook.getSheetAt(0);
            if (sheetTrans.getLastRowNum() < 2) {
                throw new EmptyFileException();
            } else {
                for (Row row : sheetTrans) {
                    if (row.getRowNum() > 1) {
                        if (!isRowNotEmpty(row, 4, 0)) {
                            String email = getFieldByIndex(row, 0);
                            String groups = getFieldByIndex(row, 1);
                            String firstName = getFieldByIndex(row, 2);
                            String lastName = getFieldByIndex(row, 3);

                            if (email.trim().isEmpty()) {
                                errors.add("Email field required!");
                            }
                            if (!email.matches("^(.+)@(\\S+)$")) {
                                errors.add("Invalid email " + email + " format!");
                            }
                            if(userService.existsByEmail(email)){
                                errors.add("Email " + email + " already exists");
                            }
                            if (errors.size() > 0) {
                                response.setSuccess(false);
                                response.setErrors(errors);
                                return response;
                            } else {
                                User user = new User();
                                user.setEmail(email);
                                if (!firstName.trim().isEmpty()) {
                                    user.setFirstname(firstName);
                                }
                                if (!lastName.trim().isEmpty()) {
                                    user.setLastname(lastName);
                                }
                                if(!groups.trim().isEmpty()){
                                    String[] split = groups.split("-");
                                    Arrays.stream(split).forEach(group -> {
                                        Optional<Group> optionalGroup = groupService.findGroupByNameIgnoreCase(projectId, group);
                                        if (optionalGroup.isPresent()) {
                                            user.getGroups().add(optionalGroup.get());
                                        } else {
                                            Group newGroup = new Group();
                                            newGroup.setName(group);
                                            try {
                                                newGroup = groupService.saveEntity(newGroup, projectId);
                                            } catch (NotFoundException e) {
                                                throw new RuntimeException(e);
                                            }
                                            user.getGroups().add(newGroup);
                                        }

                                    });
                                }
                                userService.save(user, projectId);
                            }
                        } else {
                            response.setSuccess(false);
                        }
                    }
                }
            }
            response.setSuccess(true);
            response.setErrors(errors);
            return response;
        } catch (EmptyFileException e) {
            throw new EmptyFileException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            inputWorkbook.close();
        }


    }

    public XSSFSheet readFile(InputStream inputStream) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() == -1) {
                throw new EmptyFileException();
            }
            return sheet;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    private XSSFWorkbook getWorkbookFile(String filePath) throws IOException {
        if (!filePath.endsWith(".csv")) {
            FileInputStream input = new FileInputStream(filePath);
            return new XSSFWorkbook(input);
        } else {
            String xlsxFileAddress = filePath.replace(".csv", ".xlsx");
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet sheet = workBook.createSheet("data");
            String currentLine;
            int RowNum = 0;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((currentLine = br.readLine()) != null) {
                String str[] = currentLine.split(",");
                RowNum++;
                XSSFRow currentRow = sheet.createRow(RowNum);
                for (int i = 0; i < str.length; i++) {
                    currentRow.createCell(i).setCellValue(str[i]);
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(xlsxFileAddress);
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            return workBook;
        }
    }


    public static boolean isRowNotEmpty(Row row, int size, int startCell) {
        boolean isEmpty = true;
        //DataFormatter dataFormatter = new DataFormatter();

        if (row != null) {
            for (int i = startCell; i < size + startCell; i++) {

                //if (row.getCell(i) != null && dataFormatter.formatCellValue(row.getCell(i)).trim().length() > 0) {
                if (row.getCell(i) != null) {
                    isEmpty = false;

                } else {
                    isEmpty = true;
                    break;

                }
            }

        }

        return isEmpty;
    }*/
}
