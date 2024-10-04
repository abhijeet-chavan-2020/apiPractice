package com.test;

public class CoforgeTest {
    public static void main(String[] args) {
        String name="My Name IS AbhiJeet";
        int len= name.length();
        String upper=name.toUpperCase();
        StringBuilder sb= new StringBuilder(name);

        for(int i=0; i<len; i++){
            if(upper.charAt(i)==name.charAt(i)){
                System.out.println(name.charAt(i));
            }

        }
    }
}
