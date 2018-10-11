package hit.to.go.entity.user;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/9/18
 */
public class User {
    public static final int TYPE_ADMIN = 2;
    public static final int TYPE_TEACHER = 3;
    public static final int TYPE_STUDENT = 4;
    public static final int TYPE_VISITOR = 5;
    public static final int TYPE_FORBIDDEN_USER = 6;

    public static final String DEFAULT_VISITOR_NAME = "默认游客名";

    private Integer id;
    private String name;
    private Integer type;
    private String sex;
    private Date birthday;
    private String email;
    private Integer education;
    private String school;
    private String intro;
    private String note;
    private String phone;
    private String img;
    private boolean isVisitor;

    public User() {
        this.isVisitor = false;
    }

    public User(boolean isVisitor) {
        this.isVisitor = isVisitor;
        if (isVisitor) {
            this.id = -1;
            this.type = TYPE_VISITOR;
            this.name = DEFAULT_VISITOR_NAME;
        }
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEducation() {
        return education;
    }

    public void setEducation(Integer education) {
        this.education = education;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isVisitor() {
        return isVisitor;
    }
}
