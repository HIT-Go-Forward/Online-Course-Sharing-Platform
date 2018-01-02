from django.db import models
from django.db.models import Model
from django.db.models import OneToOneField
from django.db.models import CharField
from django.db.models import DateField
from django.db.models import ImageField
from django.contrib.auth.models import User

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
    birth_date = DateField()
    gender = CharField(max_length=1, choices=GENDER_CHOICES)
    company = CharField(max_length=100)
    degree = CharField(max_length=100)
    avatar = ImageField()
    description = CharField(max_length=10000)
