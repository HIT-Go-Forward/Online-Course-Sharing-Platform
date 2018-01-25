from os.path import splitext
from uuid import uuid4


# noinspection PyUnusedLocal
from django.core.exceptions import ValidationError


def uploaded_filename(instance, filename):
    return str(uuid4()) + splitext(filename)[1]


def validate_file_extension(value):
    if not splitext(value.name)[1].lower() in ['.png', '.jpg', '.jpeg']:
        raise ValidationError('Invalid file type')
