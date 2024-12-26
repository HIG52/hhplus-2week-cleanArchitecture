package com.hjy.lecture.hhplus2weekcleanarchitecture.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureRequestDTO {
    private long lectureId;
    private long userId;

}
