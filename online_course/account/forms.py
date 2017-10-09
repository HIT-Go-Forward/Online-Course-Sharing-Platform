from django.utils.translation import ugettext as _
from django.forms import Form
from django.forms import CharField
from django.forms import EmailField
from django.forms import TextInput
from django.forms import ValidationError
from django.contrib.auth.models import User


class RegisterForm(Form):
    username = CharField(max_length=100, required=True)
    email = EmailField(max_length=100, required=True)
    password = CharField(
        max_length=100,
        widget=TextInput(attrs={'type': 'password'}),
        required=True,
    )
    repeat_password = CharField(
        max_length=100,
        widget=TextInput(attrs={'type': 'password'}),
        required=True,
    )

    def clean_email(self):
        email = self.cleaned_data['email'].lower()
        if email:
            try:
                User.objects.get(email=email)
                raise ValidationError(_('Email already exists'))
            except User.DoesNotExist:
                return email
        else:
            raise ValidationError(_('Cannot be blank'))
