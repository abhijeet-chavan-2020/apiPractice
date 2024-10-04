package com.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostReqWithJSONArrayAsList {

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
    public void postRequestWithBodyAsJSONArray() {
        HashMap<String, String> obj1 = new HashMap<String, String>();
        obj1.put("id", "1001");
        obj1.put("type", "Type1001");

        HashMap<String, String> obj2 = new HashMap<String, String>();
        obj2.put("id", "1002");
        obj2.put("type", "Type1002");
        List<HashMap> list= new ArrayList<>() ;
        list.add(obj1);
        list.add(obj2);
        //Jackson serialization is needed to convert the Hashmap into JSON. So we need to add Jackson databind library into classpath. Gson can be used as well.
                given().
                    body(list).
                when().
                    post("/post").
                then().
                        spec(resSpec).
                    assertThat().
                    body("msg", equalTo("Success")
                    );
    }
}

