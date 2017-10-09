from django.contrib.auth import login
from django.contrib.auth.models import User
from django.http import JsonResponse
from django.shortcuts import render

from account.forms import RegisterForm


def register(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            email = form.cleaned_data['email']
            user = User.objects.create_user(email, email, form.cleaned_data['password'])
            user.save()
            login(request, user)
            return JsonResponse({'code': 0})
        else:
            return JsonResponse({'code': -100, 'message': form.errors})
    else:
        return render(request, 'account/register.html', {'form': RegisterForm})
