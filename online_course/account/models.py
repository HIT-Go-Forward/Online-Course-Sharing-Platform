from datetime import date

from django.contrib.auth.models import User
from django.db import models
from django.db.models import CharField, DateField, ImageField, Model, OneToOneField, DateTimeField

from online_course.utils import uploaded_filename, validate_file_extension

ACCOUNT_TYPE_CHOICES = (
    ('s', '学生'),
    ('t', '教师'),
)

GENDER_CHOICES = (
    ('m', '男'),
    ('f', '女'),
)


class UserProfile(Model):
    user = OneToOneField(User, on_delete=models.CASCADE)
    account_type = CharField(max_length=1, choices=ACCOUNT_TYPE_CHOICES)
    birth_date = DateField(default=date.today)
    gender = CharField(max_length=1, choices=GENDER_CHOICES)
    company = CharField(max_length=100)
    degree = CharField(max_length=100)
    avatar = ImageField(upload_to=uploaded_filename, validators=[validate_file_extension])
    description = CharField(max_length=10000)


class TeacherApplication(Model):
    user = OneToOneField(User, on_delete=models.CASCADE)
    timestamp = DateTimeField(auto_now_add=True)
