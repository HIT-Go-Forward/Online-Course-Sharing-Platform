from datetime import date
from uuid import uuid4
from os.path import splitext

from django.db import models
from django.db.models import Model
from django.db.models import OneToOneField
from django.db.models import CharField
from django.db.models import DateField
from django.db.models import ImageField
from django.contrib.auth.models import User
from django.core.exceptions import ValidationError

ACCOUNT_TYPE_CHOICES = (
    ('s', '学生'),
    ('t', '教师'),
)

GENDER_CHOICES = (
    ('m', '男'),
    ('f', '女'),
)


# noinspection PyUnusedLocal
def uploaded_filename(instance, filename):
    return str(uuid4()) + splitext(filename)[1]


def validate_file_extension(value):
    if not splitext(value.name)[1] in ['.png', '.jpg', '.jpeg']:
        raise ValidationError('Invalid file type')


class UserProfile(Model):
    user = OneToOneField(User, on_delete=models.CASCADE)
    account_type = CharField(max_length=1, choices=ACCOUNT_TYPE_CHOICES)
    birth_date = DateField(default=date.today)
    gender = CharField(max_length=1, choices=GENDER_CHOICES)
    company = CharField(max_length=100)
    degree = CharField(max_length=100)
    avatar = ImageField(upload_to=uploaded_filename, validators=[validate_file_extension])
    description = CharField(max_length=10000)
