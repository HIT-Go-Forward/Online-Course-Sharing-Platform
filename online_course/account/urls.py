from django.conf.urls import url, include
from django.views.generic import TemplateView
from rest_framework.routers import DefaultRouter

from account.views.avatar import AvatarViewSet
from account.views.change_password import change_password
from account.views.login import login_authenticate
from account.views.logout import logout_view
from account.views.profile import profile_settings, profile_view
from account.views.register import register
from account.views.teacher_apply import teacher_apply

router = DefaultRouter()
router.register(r'settings/profile/avatar', AvatarViewSet, 'avatar')

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^register/$', register, name='register'),
    url(r'^login/$', login_authenticate, name='login'),
    url(r'^logout/$', logout_view, name='logout'),
    url(r'^settings/profile/$', profile_settings, name='profile_settings'),
    url(r'^settings/change_password/$', change_password, name='change_password'),
    url(r'^center/$', profile_view, name='center'),
    url(r'^teacher_apply/$', teacher_apply, name="teacher_apply"),
    url(r'^apply_manage', TemplateView.as_view(template_name='account/manager/apply_manage.html'),
        name="apply_manage"),
    url(r'^course_control',
        TemplateView.as_view(template_name='account/teacher/course_control.html'),
        name="course_control"),
    url(r'^my_course', TemplateView.as_view(template_name='account/student/my_course.html'),
        name="my_course"),
]
