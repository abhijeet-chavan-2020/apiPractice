package com.test;

import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;

public class FileDownloadUsingAPI {

    //Here, we will try to download a file from API. We can store the extracted response as a class type, as InputStream, as byteArray, as String and as PrettyString.
    // We will use FileOutputStream class to write the content into the file. It accepts file object as Input parameter while initializing.
    // the write() method of FileOutputStream accepts byte[] byteArray as input parameter. So the content that we read from the downloaded file needed to be converted into byte[].
    // In the first test , the response is extracted as asByteArray() so no need to convert it.
    //In the second test, the response is extracted as asInputStream(). So there we have converted the InputStream to byte[] using byte[] bytes= inputeStreamObj.readAllBytes();
    @Test
    public void multiPartFormDataWithFileDownloadWithByteArray() throws IOException {

        byte[] bytes = given().
                baseUri("https://github.com").
                log().all().
                when().
                get("/abhijeet-chavan-2020/TestNgPractice/raw/refs/heads/main/src/test/resources/TestData/DataProviderTestData.xlsx").
                then().
                log().all().
                extract().response().asByteArray();

        File file = new File("DataProviderTestData.xlsx");
        if (!file.exists() || file.delete()) {
            file.setWritable(true);
            try {
                OutputStream out = new FileOutputStream(file);
                out.write(bytes);
                out.close();
                System.out.println("File downloaded successfully.");
            } catch (Exception e) {
                System.out.println("Error downloading the file:");
            }
        } else {
            System.out.println("Could not delete the existing file.");
        }
    }


    @Test
    public void multiPartFormDataWithFileDownloadWithInputStream() throws IOException {
        InputStream inputStream =
                given().
                        baseUri("https://github.com").
                        log().all().
                        when().
                        get("/abhijeet-chavan-2020/TestNgPractice/raw/refs/heads/main/src/test/resources/TestData/DataProviderTestData.xlsx").
                        then().
                        log().all().
                        extract().response().asInputStream();

        byte[] bytes = inputStream.readAllBytes();

        File file = new File("DataProviderTestData.xlsx");
        if (!file.exists() || file.delete()) {
            file.setWritable(true);
            try {
                OutputStream out = new FileOutputStream(file);
                out.write(bytes);
                out.close();
                System.out.println("File downloaded successfully.");
            } catch (Exception e) {
                System.out.println("Error downloading the file:");
            }
        } else {
            System.out.println("Could not delete the existing file.");
        }
    }
}
