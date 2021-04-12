package com.zohar.educope.service.common;

import com.zohar.educope.constant.CourseRegisterStatus;
import com.zohar.educope.entity.AttendanceCourse;
import java.util.List;

public interface AttendanceCourseService {

  AttendanceCourse createAttendanceCourse(AttendanceCourse attendanceCourse);

  List<AttendanceCourse> getAttendanceCourseListByTypeAndCourseId(
      CourseRegisterStatus attendanceCourseStatus, String courseId);

  AttendanceCourse updateAttendanceStatus(AttendanceCourse attendanceCourse);

}
