package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.rest.pojo.simple.SimplePojo;
import com.rest.pojo.simple.Workspace;
import com.rest.pojo.simple.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class SimpleWorkspacePojoTest {

    RequestSpecification reqSpec;
    ResponseSpecification resSpec;
    List<String> workspaceId= new ArrayList<>();
    @BeforeClass
    public void beforeClass() {
        //created post request and response both Default specification builder in the before class
        HashMap<String, String> h1 = new HashMap<String, String>();
        h1.put("X-Api-key", "PMAK-664f878cdb04f90001b15727-bf0f71d79548fe4d749682d8bca6988c0f");

        System.out.println("Printing Request Specification");
        RequestSpecBuilder reqSpecBldr = new RequestSpecBuilder()
                .setBaseUri("https://api.postman.com")
                .addHeaders(h1)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
        reqSpec =  reqSpecBldr.build().log().all();

        System.out.println("Printing Response Specification");

        ResponseSpecBuilder responseSpecBuilder= new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.ALL);
        resSpec=  responseSpecBuilder.build();

    }

    @Test(dataProvider = "createWorkspaceData")
    public void createWorkspaceUsingPojo(String[] data) {
        System.out.println("workspaceName = " + data[0]);
        Workspace workspace = new Workspace(data[0], data[1], data[2]);
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);

        WorkspaceRoot deserializedWorkspaceRoot =
                given().spec(reqSpec).
                        body(workspaceRoot).
                        when().
                        post("/workspaces/").
                        then().
                        spec(resSpec).
                        extract().
                        response().
                        as(WorkspaceRoot.class);

        assertThat(deserializedWorkspaceRoot.getWorkspace().getName(), equalTo(workspaceRoot.getWorkspace().getName()));
        assertThat(deserializedWorkspaceRoot.getWorkspace().getId(), matchesPattern("[a-z0-9-]{36}$"));
        workspaceId.add(deserializedWorkspaceRoot.getWorkspace().getId());
    }

    @Test(dataProvider = "deleteWorkspacesData", dependsOnMethods = "createWorkspaceUsingPojo")
    public void deleteMethodWithBodyAsStringBDD(String id) {
        //Use Payload as Body in String format.
        given(). spec(reqSpec).
                pathParam("workspaceId",id).
                when().
                delete("/workspaces/{workspaceId}"). // the value of workspaceId will be resolved during execution.
                then().
                log().all().
                assertThat().
                body("workspace.id", equalTo(id)
                );
    }

    @DataProvider
    public Object[][] createWorkspaceData(){
        return new Object[] []{
            {new Faker().name().fullName().toUpperCase(), "personal", "Rest assured created this using nested json object as request payload- data 1" },
            {new Faker().name().fullName().toUpperCase(), "personal", "Rest assured created this using nested json object as request payload- data 1" },
            {new Faker().name().fullName().toUpperCase(), "personal", "Rest assured created this using nested json object as request payload - data 3" }
        };
    }

    @DataProvider
    public Iterator<String> deleteWorkspacesData(){
        Iterator<String> it= workspaceId.iterator();
        return it;
    }
}

