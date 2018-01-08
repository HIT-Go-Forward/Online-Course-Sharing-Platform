from django.contrib.auth import authenticate
from django.contrib.auth import login
from django.http import JsonResponse
from django.shortcuts import render
from django.forms import Form
from django.forms import CharField
from django.forms import TextInput
from django.urls import reverse


def login_authenticate(request):
    if request.method == 'POST':
        form = LoginForm(request.POST)
        if form.is_valid():
            username = form.cleaned_data['username']
            password = form.cleaned_data['password']
            user = authenticate(request, username=username, password=password)
            if user is not None:
                login(request, user)
                return JsonResponse({'code': 0})
            return JsonResponse({'code': -200, 'message': '用户名或密码错误'})
        return JsonResponse({'code': -100, 'message': form.errors})
    else:
        return render(
            request,
            'account/login.html', {
                'form': LoginForm(),
                'next': request.GET.get('next', reverse('index')),
            },
        )


class LoginForm(Form):
    username = CharField(
        max_length=100,
        required=True,
        widget=TextInput(attrs={'type': 'text', 'class': 'input form-control'})
    )
    password = CharField(
        max_length=100,
        widget=TextInput(attrs={'type': 'password', 'class': 'input form-control'}),
        required=True,
    )

    def clean_email(self):
        return self.cleaned_data['email'].lower()
