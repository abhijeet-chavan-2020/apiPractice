package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class SerializationDeserialization {

    //Converting java object into JSON/XML is called Serialization and vice versa called De-Serialization.
    //RestAssured can serialize a POJO to JSON, a HashMap to JSON and also a List to JSON
    //Rest Assured supports Content-Type based Serialization. If Content-Type= application.json then it will first try to use Jackson if found in classspath, if not then Gson.
    // If Content-type= application.xml, then Rest Assured will serialize to XML using JAXB.
    //If no content-type is defined then Rest Assured will try to serialize in the following order:
    // 1 - JSON using Jackson 2 (Faster Jackson databind)
    // 2 - JSON using Jackson (Databind)
    // 3 - JSON using Gson
    // 4 - JSON using Johnzon
    // 5 - JSON-B using Eclipse Yasson
    // 6 - XML using JAXB
    // Also we can explicitly tell Rest Assured to use which Serializer it should be using.
    // Jackson Databind has ObjectMapper class which is a powerful class. By the following way we can create an object of ObjectMapper
   // HashMap<String, String> hashMap= new HashMap<>();
    // ObjectMapper objectMapper = new ObjectMapper();
    // Now by using method writeValueAsString from ObjectMapper by providing an object of a Map class., the Serialization is completed. This method return ObjectWriter instance.
    //This method is used to serialize any Java value as a String.
    // All these things are internally done by RestAssured using Jackson databind library. We just need to provide the HashMap object.

    /*
    Serialize Object Node to JSON :
    Object Node is similar to HashMap. Using ObjectMapper class object and calling createObjectNode() method will return instance of ObjectNode class.
    it has put(String, T) method. Using this we can create an ObjectNode just like the HashMap.
     */

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    String workspaceId = "";
    String workspaceName = "";

    @BeforeClass
    public void beforeClass() {
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-bf0f71d79548fe4d749682d8bca6988c0f");
        h1.put("x-mock-match-request-headers", "true");

        System.out.println("Printing Request Specification");
        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
                .setBaseUri("https://de596966-7459-4f6f-868d-0ab555d3939c.mock.pstmn.io")
                .addHeaders(h1)
                .setContentType(ContentType.JSON)
                .setConfig(config.encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType("charset","UTF-8")))
                .log(LogDetail.ALL);
        RestAssured.requestSpecification = reqSpecBldr.build().log().all();

        System.out.println("Printing Response Specification");

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.ALL);
        resSpec= responseSpecBuilder.build();
    }

    @Test
    public void createWorkspaceUsingObjectNode() throws JsonProcessingException {
        Faker fk = new Faker();
        workspaceName = fk.name().fullName().toUpperCase().replaceAll(" ", "") + fk.number().digits(5);
        System.out.println("workspaceName = " + workspaceName);

        ObjectMapper objectMapper= new ObjectMapper();
        ObjectNode objectNode= objectMapper.createObjectNode();
        objectNode.put("name", workspaceName);
        objectNode.put("type", "personal");
        objectNode.put("description", "Rest assured created this using nested json object as request payload");

        ObjectNode mainObjectNode= objectMapper.createObjectNode();

        mainObjectNode.put("workspace", objectNode);

        String bodyString= objectMapper.writeValueAsString(mainObjectNode);
        workspaceId =
                given().
                        body(bodyString). // Here, Rest Assured will not use serialization because using Jackson Databind we have achieved serialization process.
                        when().
                        post("/workspaces").
                        then().
                        assertThat().
                        body("workspace.name", equalTo(workspaceName)
                        ).
                        extract().response().getBody().jsonPath().getString("workspace.id");
        System.out.println("workspaceId = " + workspaceId);
    }

    @Test
    public void createWorkspaceUsingArrayNode() throws JsonProcessingException {
        //Here, an ObjectNode object is created and added 2 key value pairs in it using put method.
        // Then using ArrayNode, (which acts as a list), we are adding these 2 ObjectNode objects into the ArrayNode list using add(objectNode) method.
        //In this way, ArrayNode of 2 ObjectNodes are created.
        //Now by using ObjectNode.writeValueAsString(ArrayNode object) method, we are creating a string variable and then we will be passing this string to the body of the request.
        // In this way, Rest Assured does not need to serialize the given string.
        Faker fk = new Faker();
        workspaceName = fk.name().fullName().toUpperCase().replaceAll(" ", "") + fk.number().digits(5);
        System.out.println("workspaceName = " + workspaceName);

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode1001 = objectMapper.createObjectNode();
        objectNode1001.put("id", "1001");
        objectNode1001.put("type", "Type1001");

        ObjectNode objectNode1002 = objectMapper.createObjectNode();
        objectNode1002.put("id", "1002");
        objectNode1002.put("type", "Type1002");

        ArrayNode arrayNodeList= objectMapper.createArrayNode();

        arrayNodeList.add(objectNode1001);
        arrayNodeList.add(objectNode1002);

        String bodyString= objectMapper.writeValueAsString(arrayNodeList);

        given().
                body(bodyString).
        when().
                post("/post").
        then().
                spec(resSpec).
                assertThat().
                body("msg", equalTo("Success")
                );
    }

}
