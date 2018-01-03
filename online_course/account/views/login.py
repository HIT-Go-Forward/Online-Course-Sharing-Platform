from django.utils.translation import ugettext as _
from django.contrib.auth import authenticate
from django.contrib.auth import login
from django.http import JsonResponse
from django.shortcuts import render
from django.forms import Form
from django.forms import CharField
from django.forms import TextInput


def login_authenticate(request):
    if request.method == 'POST':
        form = LoginForm(request.POST)
        if form.is_valid():
            email = form.cleaned_data['email']
            password = form.cleaned_data['password']
            print(email+" "+password)
            user = authenticate(request, email=email, password=password)#这儿应该是email
            if user is not None:
                login(request, user)
                return JsonResponse({'code': 0})
            return JsonResponse({'code': -200, 'message': _('Wrong email or password')})
        return JsonResponse({'code': -100, 'message': form.errors})
    else:
        return render(request, 'account/login.html', {'form': LoginForm()})


class LoginForm(Form):
    email = CharField(max_length=100, required=True,widget=TextInput(attrs={'type': 'email','class':'input form-control'}))
    password = CharField(
        max_length=100,
        widget=TextInput(attrs={'type': 'password','class':'input form-control'}),
        required=True,
    )

    def clean_email(self):
        return self.cleaned_data['email'].lower()
