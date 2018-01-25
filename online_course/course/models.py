from django.contrib.auth.models import User
from django.db import models

from online_course.utils import uploaded_filename, validate_file_extension


class Course(models.Model):
    title = models.CharField(max_length=100)
    description = models.TextField(null=True)
    date_start = models.DateField(null=True)
    date_end = models.DateField(null=True)
    cover = models.ImageField(
        upload_to=uploaded_filename, null=True, validators=[validate_file_extension])
    students = models.ManyToManyField(User, related_name='courses_learning')
    teacher = models.ForeignKey(User, related_name='courses_teaching')
