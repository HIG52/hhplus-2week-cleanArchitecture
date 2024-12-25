package com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query("""
        SELECT l FROM Lecture l
        WHERE FUNCTION('DATE', l.lectureStartDateTime) = FUNCTION('DATE', :lectureStartDateTime)
        AND (
            SELECT COUNT(ls.lectureId.userId)
            FROM LectureStudent ls
            WHERE ls.lectureId.lectureId = l.lectureId
        ) < 30
    """)
    List<Lecture> findLecturesStartingOnDateWithAvailableSeats(
            @Param("lectureStartDateTime") LocalDateTime lectureStartDateTime
    );
}
