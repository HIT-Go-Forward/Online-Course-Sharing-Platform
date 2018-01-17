# Style Setting

The style of form should be modified in *custom Form class* with **WIDGET**.

```
class MyForm(forms.ModelForm):
    class Meta:
        model = MyModel
        widgets = {
            'myfield': forms.TextInput(attrs={'class': 'myfieldclass'}),
        }
```

*For more:* [Click here](https://stackoverflow.com/questions/5827590/css-styling-in-django-forms)
