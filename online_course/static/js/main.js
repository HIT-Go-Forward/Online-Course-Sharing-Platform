function judge_page() {
    list = document.location.href.split('/');
    local = list[list.length - 2];
    if (local === "profile") {
        $("#profile-settings").addClass('active');
    }
    else if (local === "center") {
        $("#center").addClass('active');
    }
    else if (local === "change_password") {
        $("#change-password").addClass("active");
    }
    else if (local === "apply_teacher") {
        $("#apply-teacher").addClass("active");
    }
    else if (local === "course_control") {
        $("#course-control").addClass("active");
    }
    else if(local==="apply_manage"){
        $("#apply-manage").addClass("active");
    }
    $("input[type='password']").addClass("input form-control");
    $("input[type='text']").addClass("input form-control");
    $("select").addClass("form-control");
}
