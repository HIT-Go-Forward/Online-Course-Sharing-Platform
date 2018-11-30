package hit.go.forward.entity.resource;

import java.util.Date;

/**
 * Created by 班耀强 on 2018/10/20
 */
public class Resource {
    public static final Integer TYPE_IMG = 1;
    public static final Integer TYPE_VIDEO = 2;
    public static final Integer TYPE_OTHER = 3;

    private Integer id;
    private String name;
    private Integer userId;
    private Integer type;
    private String note;
    private String url;
    private Date date;
    private Integer lessonId;
    private String intro;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
