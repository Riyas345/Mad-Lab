package com.example.parser;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    Button xmlButton, jsonButton;
    TextView xmlResult, jsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // --------------------------------------------------
        // HANDLE STATUS BAR / CAMERA / DISPLAY CUTOUT
        // --------------------------------------------------

        LinearLayout mainLayout =
                findViewById(R.id.mainLayout);

        ViewCompat.setOnApplyWindowInsetsListener(
                mainLayout,
                (view, windowInsets) -> {

                    Insets insets = windowInsets.getInsets(
                            WindowInsetsCompat.Type.statusBars()
                                    | WindowInsetsCompat.Type.displayCutout()
                    );

                    view.setPadding(
                            10,
                            insets.top + 10,
                            10,
                            10
                    );

                    return windowInsets;
                }
        );


        // --------------------------------------------------
        // CONNECT UI COMPONENTS
        // --------------------------------------------------

        xmlButton = findViewById(R.id.xmlButton);

        jsonButton = findViewById(R.id.jsonButton);

        xmlResult = findViewById(R.id.xmlResult);

        jsonResult = findViewById(R.id.jsonResult);


        // --------------------------------------------------
        // XML BUTTON
        // --------------------------------------------------

        xmlButton.setOnClickListener(v -> {

            parseXML();

        });


        // --------------------------------------------------
        // JSON BUTTON
        // --------------------------------------------------

        jsonButton.setOnClickListener(v -> {

            parseJSON();

        });
    }


    // ======================================================
    // XML PARSING
    // ======================================================

    private void parseXML() {

        try {

            // Open XML file from assets
            InputStream inputStream =
                    getAssets().open("data.xml");


            // Create XML parser
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();


            // Parse XML
            Document document =
                    builder.parse(inputStream);

            document.getDocumentElement().normalize();


            // Get all student elements
            NodeList nodeList =
                    document.getElementsByTagName("student");


            StringBuilder result =
                    new StringBuilder();


            // Read each student
            for (int i = 0;
                 i < nodeList.getLength();
                 i++) {

                Node node =
                        nodeList.item(i);


                if (node.getNodeType() ==
                        Node.ELEMENT_NODE) {

                    Element element =
                            (Element) node;


                    String name =
                            element
                                    .getElementsByTagName("name")
                                    .item(0)
                                    .getTextContent();


                    String age =
                            element
                                    .getElementsByTagName("age")
                                    .item(0)
                                    .getTextContent();


                    String department =
                            element
                                    .getElementsByTagName("department")
                                    .item(0)
                                    .getTextContent();


                    result.append("Name: ")
                            .append(name)
                            .append("\n");


                    result.append("Age: ")
                            .append(age)
                            .append("\n");


                    result.append("Department: ")
                            .append(department)
                            .append("\n\n");
                }
            }


            // Display XML data
            xmlResult.setText(
                    result.toString()
            );


            inputStream.close();

        }

        catch (Exception e) {

            xmlResult.setText(
                    "Error parsing XML:\n"
                            + e.getMessage()
            );
        }
    }


    // ======================================================
    // JSON PARSING
    // ======================================================

    private void parseJSON() {

        try {

            // Open JSON file from assets
            InputStream inputStream =
                    getAssets().open("data.json");


            // Read file
            int size =
                    inputStream.available();


            byte[] buffer =
                    new byte[size];


            inputStream.read(buffer);

            inputStream.close();


            // Convert to String
            String json =
                    new String(buffer, "UTF-8");


            // Create JSON object
            JSONObject jsonObject =
                    new JSONObject(json);


            // Get students array
            JSONArray students =
                    jsonObject.getJSONArray("students");


            StringBuilder result =
                    new StringBuilder();


            // Read each student
            for (int i = 0;
                 i < students.length();
                 i++) {

                JSONObject student =
                        students.getJSONObject(i);


                String name =
                        student.getString("name");


                int age =
                        student.getInt("age");


                String department =
                        student.getString("department");


                result.append("Name: ")
                        .append(name)
                        .append("\n");


                result.append("Age: ")
                        .append(age)
                        .append("\n");


                result.append("Department: ")
                        .append(department)
                        .append("\n\n");
            }


            // Display JSON data
            jsonResult.setText(
                    result.toString()
            );

        }

        catch (Exception e) {

            jsonResult.setText(
                    "Error parsing JSON:\n"
                            + e.getMessage()
            );
        }
    }
}