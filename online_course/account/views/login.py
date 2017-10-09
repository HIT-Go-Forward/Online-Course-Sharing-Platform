from django.utils.translation import ugettext as _
from django.contrib.auth import authenticate
from django.contrib.auth import login
from django.http import JsonResponse
from django.shortcuts import render

from account.forms import LoginForm


def login_authenticate(request):
    if request.method == 'POST':
        form = LoginForm(request.POST)
        if form.is_valid():
            email = form.cleaned_data['email']
            password = form.cleaned_data['password']
            user = authenticate(request, username=email, password=password)
            if user is not None:
                login(request, user)
                return JsonResponse({'code': 0})
            return JsonResponse({'code': -200, 'message': _('Wrong email or password')})
        return JsonResponse({'code': -100, 'message': form.errors})
    else:
        return render(request, 'account/login.html', {'form': LoginForm()})
