package com.hjy.lecture.hhplus2weekcleanarchitecture.infrastructure.repository;

import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.Lecture;
import com.hjy.lecture.hhplus2weekcleanarchitecture.domain.entity.LectureStudent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLectureRepository extends JpaRepository<Lecture, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lecture l WHERE l.lectureId = :lectureId")
    Optional<Lecture> findByIdWithLock(@Param("lectureId") Long lectureId);

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

    @Query("""
        SELECT l FROM LectureStudent ls
        JOIN Lecture l ON l.lectureId = ls.lectureId.lectureId
        WHERE ls.lectureId.userId = :userId
    """)
    List<Lecture> findLecturesAppliedByUserId(@Param("userId") Long userId);
}
