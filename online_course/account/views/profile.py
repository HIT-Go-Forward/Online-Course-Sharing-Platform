from django.forms import Form
from django.forms import CharField
from django.forms import DateField
from django.forms import ChoiceField
from django.forms import ImageField
from django.shortcuts import render
from django.contrib.auth.decorators import login_required
from django.urls import reverse_lazy

GENDER_CHOICES = (
    ('m', 'Male'),
    ('f', 'Female'),
)


@login_required(login_url=reverse_lazy('login'))
def profile_settings(request):
    return render(request, 'account/profile_settings.html', {'form': ProfileForm()})


class ProfileForm(Form):
    username = CharField(disabled=True, label='用户名')
    account_type = CharField(disabled=True, label='身份')
    birth_date = DateField(label='出生日期')
    gender = ChoiceField(choices=GENDER_CHOICES, label='性别')
    email = CharField(disabled=True, label='邮箱')
    company = CharField(max_length=100, label='公司/学校')
    degree = CharField(max_length=100, label='学历')
    avatar = ImageField(label='头像')
    description = CharField(max_length=10000, label='个人信息')
