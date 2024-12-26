package com.hjy.lecture.hhplus2weekcleanarchitecture.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LectureConfig {

    @Value("${lecture.max.students:30}")
    private int maxStudents;

    public int getMaxStudents() {
        return maxStudents;
    }
}
