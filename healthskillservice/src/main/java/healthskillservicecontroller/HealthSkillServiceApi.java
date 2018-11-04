package healthskillservicecontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import com.opencsv.CSVReader;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.ResourceUtils;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;


/**
 * Created by lujingyang on 4/11/18.
 */

@RestController
public class HealthSkillServiceApi {
    @RequestMapping(value = "index" ,method = RequestMethod.GET)
    public String index(){
        return "Hello Guys!";
    }

    @RequestMapping(value = "skill/health" ,method = RequestMethod.POST)
    public String healthSkillMappingService( @RequestParam("file") MultipartFile mfile) throws IllegalStateException, IOException, InterruptedException, ClassNotFoundException{
        if (mfile.isEmpty()) {
            return "The file is empty!";
        }

        String path = ResourceUtils.getURL("classpath:").getPath() + mfile.getOriginalFilename();
//        System.out.println(path);
        File file = new File(path);
        mfile.transferTo(file);

        ArrayList<Customer> customersList = parseCustomerCSV(path);

        /*for (Customer c : customersList) {
            System.out.println(c.toString());
        }*/

        ArrayList<HealthSkillMap> healthSkillList = new ArrayList<>();
        String result = "";

        for (Customer c : customersList) {
            String id = c.getId();
            String skill = mapToSkill(c);
            HealthSkillMap hsm = new HealthSkillMap(id, skill);
            healthSkillList.add(hsm);
//            System.out.println("Customer ID is: " + hsm.getCustomerId() + " and the skill code is: " + hsm.getSkill());
            result = result + "<p>" + id + "&nbsp&nbsp&nbsp" + skill + "</p>";
        }

        String heathSkillCSVPath = ResourceUtils.getURL("classpath:").getPath() + "health_skills.csv";

        try {
            saveToCSV(healthSkillList, heathSkillCSVPath);
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }


    private ArrayList<Customer> parseCustomerCSV(String path) throws RuntimeException, IllegalStateException, IOException, InterruptedException, ClassNotFoundException{

        File file = new File(path);
        FileReader fReader = new FileReader(file);
        CSVReader reader = new CSVReader(fReader);

        ArrayList<Customer> customersList = new ArrayList<>();

        List<String[]> list = reader.readAll();
        int line = 1;
        for(String[] ss : list){
            if(line>1) {
                int item = 1;
                String id = "";
                String postcode = "";
                String age = "";
                String gender = "";
                String hasProvider = "";
                String hasChildren = "";
                String maritalStatus = "";
                String coverType = "";

                for (String s : ss) {
                    if (null != s && !s.equals("")) {
                        System.out.print(s + " , ");
                        switch (item){
                            case 1:
                                id = s;
                                break;
                            case 2:
                                postcode = s;
                                break;
                            case 3:
                                age = s;
                                break;
                            case 4:
                                gender = s;
                                break;
                            case 5:
                                hasProvider = s;
                                break;
                            case 6:
                                hasChildren = s;
                                break;
                            case 7:
                                maritalStatus = s;
                                break;
                            case 8:
                                coverType = s;
                                break;
                        }
                    }
                    item++;
                }
                System.out.println(line);
                Customer c = new Customer(id, postcode, age, gender, hasProvider, hasChildren, maritalStatus, coverType);
                customersList.add(c);
            }
            line++;
        }

        /*HeaderColumnNameMappingStrategy<Customer> mapper = new HeaderColumnNameMappingStrategy<Customer>();
        mapper.setType(Customer.class);
        CsvToBean<Customer>  csvToBean = new CsvToBean<Customer>();

        List<Customer> customersList = csvToBean.parse(mapper, reader);*/

        reader.close();

       /* for (Customer c : customersList) {
            System.out.println(c.toString());
        }*/

        return customersList;
    }

    private String mapToSkill(Customer customer){
        String skillCode = "";
        if (customer.isHasProvider().equals("0")){
            skillCode = "S001";
        }else {
            int postcode = Integer.parseInt(customer.getPostcode());
            if (postcode >= 2000 && postcode < 5000) {
                if (customer.getCoverType().equals("combined")){
                    skillCode = "S002";
                }else if(customer.isHasChildren().equals("1") && customer.getMaritalStatus().equals("married")){
                    skillCode = "S001";
                }else {
                    skillCode = "S004";
                }
            }else if (customer.isHasChildren().equals("1") && customer.getMaritalStatus().equals("single")){
                skillCode = "S003";
            }else {
                skillCode = "S004";
            }
        }

        return skillCode;
    }

    private void saveToCSV(ArrayList<HealthSkillMap> healthSkillMapArrayList, String heathSkillCSVPath) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException{
        Writer writer = new FileWriter(heathSkillCSVPath);

        writer.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));


        String[] columnMapping = { "customerId", "skill"};
        ColumnPositionMappingStrategy<HealthSkillMap> mapper =
                new ColumnPositionMappingStrategy<HealthSkillMap>();
        mapper.setType(HealthSkillMap.class);
        mapper.setColumnMapping(columnMapping);

        CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER);
        String[] header = { "id", "skill"};
        csvWriter.writeNext(header);

        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withMappingStrategy(mapper)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withEscapechar(',').build();
        beanToCsv.write(healthSkillMapArrayList);
        csvWriter.close();
        writer.close();

    }

}
