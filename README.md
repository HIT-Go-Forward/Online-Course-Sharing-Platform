# Online Course Sharing Platform

A *MOOC-like* website that allows people share and learn courses. It supports danmaku and will be deployed on CERNET.

## Documentation

### Objects

*SPECIFICATIONS*
```
Name: (The name of object)
Description:
(A paragraph of description, in Chinese)
Data List:
    1. data_1
        Description: (In Chinese)
        Data Type: (Data type in database)
        Note: (OPTIONAL, in Chinese)
    2. data_2
    ...
Note: (OPTIONAL, in Chinese)
```

* Student
```
Name: Student
Description:
学生，网站的主要使用者，可以学习课程，不能开放课程
Data List:
    1. name
        Description: 姓名
        Data Type: (Data type in database)
        Note: (OPTIONAL, in Chinese)
```

* Teacher
* Admin
 
### Prototypes & Data Interfaces

*SPECIFICATIONS*
```
Prototype: (An image)
Page: (Filename of page)
Description:
(A paragraph of description, in Chinese)
Data List:
    (Data transferred between frontend and backend)
    1. data_1
        Description: (In Chinese)
        Data Type: (Data type in front end)
        Note: (OPTIONAL, in Chinese)
    2. data_2
    ...
Note: (OPTIONAL, in Chinese)
    
 ```

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

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
