package com.test;

import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;

public class LogDetailsToFile {
        //Filter is an interface. RequestLoggingFilter is a class which implements Filter interface.
    // This class has multiple constructors and one of which accepts PrintStream object. Using this object we can log details to a file.
    //we use filter() method from RestAssured class, pass an object of RequestLoggingFilter/ ResponseLoggingFilter along with the PrintStream object to write to the log file.

    @Test
    public void loggingToFile() {
        File file= new File("restassured.log");
        if(file.exists()){
            try{
                PrintStream printStream= new PrintStream(file );
                given().
                        baseUri("https://postman-echo.com/").
                        filter(new RequestLoggingFilter(LogDetail.BODY, printStream)).
                //        filter(new RequestLoggingFilter(printStream)).
                  //      filter(new ResponseLoggingFilter(printStream)).
                        filter(new ResponseLoggingFilter(LogDetail.STATUS, printStream)).
                //        log().all().
                when().
                        get("/get").
                then().
                //        log().all().
                        assertThat().
                        statusCode(200);
            } catch (Exception e){
                System.out.println("Logging to the fie is failed.");
            }
        }

    }
}
