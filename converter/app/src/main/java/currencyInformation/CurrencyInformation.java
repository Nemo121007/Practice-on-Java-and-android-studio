package currencyInformation;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Contract;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class CurrencyInformation {
    static private Map<String, Double> dictionaryСourse = new HashMap<>();
    static private Map<String, String> dictionaryFullName = new HashMap<>();
    static private List<String> listValue = new ArrayList<String>();
    static private List<String> listFullNameValue = new ArrayList<String>();

    public CurrencyInformation() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream inputStream = connection.getInputStream();
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(inputStream);
                    Map<String, Double> gettingDictionary = new HashMap<>();
                    NodeList valuteNodes = document.getElementsByTagName("Valute");
                    for (int i = 0; i < valuteNodes.getLength(); i++) {
                        Node valuteNode = valuteNodes.item(i);
                        if (valuteNode.getNodeType() == Node.ELEMENT_NODE) {
                            org.w3c.dom.Element valuteElement = (Element) valuteNode;
                            String shortName = valuteElement.getElementsByTagName("CharCode").item(0).getTextContent();
                            Double vunitRate = Double.parseDouble(valuteElement
                                    .getElementsByTagName("VunitRate").item(0)
                                    .getTextContent().replace(',', '.'));
                            dictionaryСourse.put(shortName, vunitRate);

                            String name = valuteElement.getElementsByTagName("Name").item(0).getTextContent();
                            dictionaryFullName.put(shortName, name);
                            dictionaryFullName.put(name, shortName);
                        }
                    }
                    connection.disconnect();
                    dictionaryСourse.put("RUB", 1.0);
                    dictionaryFullName.put("Российский рубль", "RUB");
                    dictionaryFullName.put("RUB", "Российский рубль");
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listValue = new ArrayList<>(dictionaryСourse.keySet());
        for (String name: listValue)
        {
            listFullNameValue.add(dictionaryFullName.get(name));
        };
    }

    public static String getFullNameValute(String name){
        return dictionaryFullName.get(name);
    }

    public static List<String> getListValute(){
        return listValue;
    }

    @NonNull
    public static List<String> getListValute(String name) {
        List<String> newList = new ArrayList<>(listValue);
        newList.remove(name);
        return newList;
    }

    public static List<String> getListFullName(){
        return listFullNameValue;
    }

    @NonNull
    public static List<String> getListFullName(String name) {
        List<String> newList = new ArrayList<>(listFullNameValue);
        newList.remove(name);
        return newList;
    }

    public static Double getCource(String nameValute){
        return dictionaryСourse.get(nameValute);
    }

    @NonNull
    public static Double getCource(String nameValuteFirst, String nameValuteSecond){
        return dictionaryСourse.get(nameValuteSecond) / dictionaryСourse.get(nameValuteFirst);
    }
}
