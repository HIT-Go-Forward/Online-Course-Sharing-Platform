package hit.go.forward.common.entity.course;

import java.util.Date;

import hit.go.forward.common.entity.file.ResourceFile;

/**
 * Created by 班耀强 on 2018/10/20
 */
public class Lesson {
    private Integer id;
    private Integer num;
    private String title;
    private Integer chapterNum;
    private String chapterTitle;
    private String intro;
    private String note;
    // private String videoUrl;
    // private String fileUrl;
    private ResourceFile video;
    private ResourceFile file;
    private String originalFileName;
    private Integer courseId;
    private Date createDate;
    private Date updateDate;
    private Integer state;



    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Integer return the num
     */
    public Integer getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Integer return the chapterNum
     */
    public Integer getChapterNum() {
        return chapterNum;
    }

    /**
     * @param chapterNum the chapterNum to set
     */
    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    /**
     * @return String return the chapterTitle
     */
    public String getChapterTitle() {
        return chapterTitle;
    }

    /**
     * @param chapterTitle the chapterTitle to set
     */
    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    /**
     * @return String return the intro
     */
    public String getIntro() {
        return intro;
    }

    /**
     * @param intro the intro to set
     */
    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * @return String return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    // /**
    //  * @return String return the videoUrl
    //  */
    // public String getVideoUrl() {
    //     return videoUrl;
    // }

    // /**
    //  * @param videoUrl the videoUrl to set
    //  */
    // public void setVideoUrl(String videoUrl) {
    //     this.videoUrl = videoUrl;
    // }

    // /**
    //  * @return String return the fileUrl
    //  */
    // public String getFileUrl() {
    //     return fileUrl;
    // }

    // /**
    //  * @param fileUrl the fileUrl to set
    //  */
    // public void setFileUrl(String fileUrl) {
    //     this.fileUrl = fileUrl;
    // }

    /**
     * @return ResourceFile return the video
     */
    public ResourceFile getVideo() {
        return video;
    }

    /**
     * @param video the video to set
     */
    public void setVideo(ResourceFile video) {
        this.video = video;
    }

    /**
     * @return ResourceFile return the file
     */
    public ResourceFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(ResourceFile file) {
        this.file = file;
    }

    /**
     * @return String return the originalFileName
     */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
     * @param originalFileName the originalFileName to set
     */
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    /**
     * @return Integer return the courseId
     */
    public Integer getCourseId() {
        return courseId;
    }

    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    /**
     * @return Date return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return Date return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return Integer return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Integer state) {
        this.state = state;
    }

}
