package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        List<Employee> datalist = new ArrayList<>();
//        datalist.add(new Employee(1, "John", "Smith", "USA", 25));
//        datalist.add(new Employee(2, "Inav", "Petrov", "RU", 23));
//
//        try (Writer writer = new FileWriter("D://Java/SpecialFile/data.csv")) {
//            StatefulBeanToCsv<Employee> sbc = new StatefulBeanToCsvBuilder<Employee>(writer).build();
//            sbc.write(datalist);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        Создали csv
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "D://Java/SpecialFile/data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        // Прочитали csv;
        List<Employee> list1 = parseXML("D://Java/SpecialFile/data.xml");
        //Прочитали xml
        String json = listToJson(list);
        String json1 = listToJson(list1);
        // Перевели в json
        writeString("D://Java/SpecialFile/data.json", json);
        writeString("D://Java/SpecialFile/data1.json", json1);
        // Записали файл

    }
    public static void writeString(String way, String json) {
        try(FileWriter file = new FileWriter(way)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static List<Employee> parseCSV(String[] title, String name) {
        try (Reader reader = new BufferedReader(new FileReader(name))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(title);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> staff = csv.parse();
            return staff;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Employee> parseXML(String name) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list1 = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory. newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse( new File(name));
        Node root = doc.getDocumentElement(); //Корень
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                String age = element.getElementsByTagName("age").item(0).getTextContent();
                Employee another = new Employee(Integer.parseInt(id), firstName, lastName, country, Integer.parseInt(age));
                list1.add(another);
            }
        }
        return list1;
    }
}