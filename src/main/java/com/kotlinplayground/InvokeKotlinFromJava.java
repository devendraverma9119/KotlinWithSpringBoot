package com.kotlinplayground;

import com.kotlinspring.entity.Course;
import com.kotlinspring.entity.Instructor;

public class InvokeKotlinFromJava {
    public static void main(String[] args) {
        var course = new Course(1,"course","name",new Instructor());
        System.out.println(course);
    }
}
