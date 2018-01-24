from django.conf.urls import url, include
from course.views import my_course

urlpatterns = [
    url(r'^my_course/$', my_course.my_course, name="my_course"),
]