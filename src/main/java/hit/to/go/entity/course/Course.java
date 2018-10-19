package hit.to.go.entity.course;

import hit.to.go.entity.user.User;
import hit.to.go.platform.SystemConfig;

/**
 * Created by 班耀强 on 2018/10/2
 */
public class Course {
    public static final int STATE_DRAFT = 1;
    public static final int STATE_VERIFYING = 2;
    public static final int STATE_RELEASED = 3;

    private Integer id;
    private String name;
//    private Integer teacherId;
    private User teacher;
    private String intro;
    private String img;
    private String note;
    private Integer state;
    private String label;
    private Integer userNum;
    private Integer lessonNum;
    private Integer grade;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTeacherId() {
        return teacher;
    }

    public void setTeacherId(User teacher) {
        this.teacher = teacher;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public Integer getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(Integer lessonNum) {
        this.lessonNum = lessonNum;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
