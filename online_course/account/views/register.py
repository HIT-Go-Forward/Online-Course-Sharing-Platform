from django.contrib.auth import login
from django.contrib.auth.models import User
from django.http import JsonResponse
from django.shortcuts import render
from django.forms import Form
from django.forms import CharField
from django.forms import EmailField
from django.forms import TextInput
from django.forms import ValidationError


def register(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            email = form.cleaned_data['email']
            username = form.cleaned_data['username']

            # Test whether the uniqueness of username and email.
            if User.objects.filter(username=username).exists():
                return JsonResponse({'code': -200})
            elif User.objects.filter(email=email).exists():
                return JsonResponse({'code': -201})

            user = User.objects.create_user(username, email, form.cleaned_data['password'])
            user.save()
            login(request, user)
            return JsonResponse({'code': 0})
        else:
            return JsonResponse({'code': -100, 'message': form.errors})
    else:
        return render(request, 'account/register.html', {'form': RegisterForm})


class RegisterForm(Form):
    username = CharField(max_length=100, required=True,widget=TextInput(attrs={'type': 'text','class':'input form-control'}))
    email = EmailField(max_length=100, required=True,widget=TextInput(attrs={'type': 'email','class':'input form-control'}))
    password = CharField(
        max_length=100,
        widget=TextInput(attrs={'type': 'password','class':'input form-control'}),
        required=True,
    )
    repeat_password = CharField(
        max_length=100,
        widget=TextInput(attrs={'type': 'password','class':'input form-control'}),
        required=True,
    )

    def clean_email(self):
        email = self.cleaned_data['email'].lower()
        if email:
            try:
                User.objects.get(email=email)
                raise ValidationError('邮箱已注册')
            except User.DoesNotExist:
                return email
        else:
            raise ValidationError('邮箱不能为空')

    def clean_repeat_password(self):
        if self.cleaned_data['password'] != self.cleaned_data['repeat_password']:
            raise ValidationError('密码不一致')
