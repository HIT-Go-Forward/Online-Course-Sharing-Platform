# Online Course Sharing Platform

A *MOOC-like* website that allows people share and learn courses. It supports danmaku and will be deployed on CERNET.

## Documentation

*WRITING*

## Installation

1. Clone this repository
```
git clone https://github.com/HIT-Go-Forward/Online-Course-Sharing-Platform.git
cd Online-Course-Sharing-Platform
```

2. Setup and activate virtual environment
```
(Linux)
python3 -m venv venv
source venv/bin/activate

(Windows PowerShell)
python -m venv venv
venv\Scripts\Activate.ps1

(Windows Command Prompt)
python -m venv venv
venv\Scripts\activate.bat
```

3. Install dependencies
```
pip install -r requirements.txt
```

4. Migrate the database
```
cd online_course
python manage.py migrate
```

## Deployment

**TODO**: *To be filled in future*


## Authors

* **Moandor** - *Main Architecture* - [Moandor-y](https://github.com/Moandor-y)
* **ANDI_Mckee** - *Backend* - [ANDI-Mckee](https://github.com/ANDI-Mckee)


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
