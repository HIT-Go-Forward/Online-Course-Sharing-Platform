package hit.to.go.entity.course;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/2
 */
public class StudyCourse extends Course {
    public static final int STATE_JOINED = 1;
    public static final int STATE_STUDYING = 2;
    public static final int STATE_STUDIED = 3;

    private Integer studyId;
    private Integer studentId;
    private Date date;
    private Integer studyState;

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStudyState() {
        return studyState;
    }

    public void setStudyState(Integer studyState) {
        this.studyState = studyState;
    }
}
