function judge_page() {
    list = document.location.href.split('/');
    if ($.inArray("profile", list) > 0) {
        $("#profile-settings").addClass('active');
    }
    else if ($.inArray("center", list) > 0) {
        $("#center").addClass('active');
    }
    else if ($.inArray("change_password", list) > 0) {
        $("#change-password").addClass("active");
    }
    else if ($.inArray("teacher_apply", list) > 0) {
        $("#apply-teacher").addClass("active");
    }
    else if ($.inArray("course_control", list) > 0) {
        $("#course-control").addClass("active");
    }
    else if ($.inArray("apply_manage", list) > 0) {
        $("#apply-manage").addClass("active");
    }
    else if ($.inArray("my_course", list) > 0) {
        $("#my-course").addClass("active");
    }
    $("input[type='password']").addClass("input form-control");
    $("input[type='text']").addClass("input form-control");
    $("select").addClass("form-control");
}
function password_judge(password1) {
    var $pwd_bar_on = $(".password-judge");
    var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
    var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
    var enoughRegex = new RegExp("(?=.{6,}).*", "g");
    if (enoughRegex.test(password1) === false) {
        $pwd_bar_on.removeClass('pwd-mid');
        $pwd_bar_on.removeClass('pwd-weak');
        $pwd_bar_on.removeClass('pwd-max');
        $pwd_bar_on.addClass('pwd-fob');
        return false;
    }
    else if (strongRegex.test(password1)) {
        $pwd_bar_on.removeClass('pwd-mid');
        $pwd_bar_on.removeClass('pwd-weak');
        $pwd_bar_on.removeClass('pwd-fob');
        $pwd_bar_on.addClass('pwd-max');
    }
    else if (mediumRegex.test(password1)) {
        $pwd_bar_on.removeClass('pwd-fob');
        $pwd_bar_on.removeClass('pwd-weak');
        $pwd_bar_on.removeClass('pwd-max');
        $pwd_bar_on.addClass('pwd-mid');
    }
    else {
        $pwd_bar_on.removeClass('pwd-mid');
        $pwd_bar_on.removeClass('pwd-fob');
        $pwd_bar_on.removeClass('pwd-max');
        $pwd_bar_on.addClass('pwd-weak');
    }
    return true;
}

function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}

function getUsername() {
    $.ajax({
            url: 'getUsername',
            data:{'username':$username.val()},
            type:"POST",
            success:function (msg) {
                if(msg==="200"){
                    $("#ajaxUsername").attr({"class":"fa fa-check"})
                }
                else{
                    $("#ajaxUsername").attr({"class":"fa fa-times"})
                }
            }
        }
    )
}
function getEmail() {
    $.ajax({
            url: 'getEmail',
            data: {'email': $email.val()},
            type: "POST",
            success: function (msg) {
                if (msg === "200") {
                    $("#ajaxEmail").attr({"class": "fa fa-check"})
                }
                else {
                    $("#ajaxEmail").attr({"class": "fa fa-times"})
                }
            }
        }
    )
}