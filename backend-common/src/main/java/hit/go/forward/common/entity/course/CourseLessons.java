package hit.go.forward.common.entity.course;

import java.util.List;

public class CourseLessons {
    private Course course;
    private List<Lesson> lessons;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
