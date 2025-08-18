# LOG-TRACK
A springboot application designed to automate and digitize the process of Internship Logbook writing and submission.

## Technologies
![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue?logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-green?logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?logo=postgresql&logoColor=white)


## Prerequisites
- Java 17+
- Maven
- Optional - API Client (Postman)
- Optional - Database Management Tool

## Installation
1. Clone the repository:
    ```bash
    https://github.com/Esosa2006/LogTrack

2. cd LogTrack

3. mvn clean install

4. mvn exec:java -Dexec.mainClass="com.example.LogTrack"

## Usage
You’ll probably need to open the main and try running a few things. It supports:
- Creation of daily Log Entries.
- Automatic Generation of weekly reports.
- Evaluation of students' entries;
- Administrator functions
- etc.

## 📁 File Structure
```powershell
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───example
│   │   │           └───LogTrack
│   │   │               ├───config
│   │   │               ├───controllers
│   │   │               ├───enums
│   │   │               ├───exceptions
│   │   │               │   └───exceptions
│   │   │               ├───mapper
│   │   │               ├───models
│   │   │               │   ├───dtos
│   │   │               │   │   ├───adminViews
│   │   │               │   │   │   └───systemInfo
│   │   │               │   │   ├───authDtos
│   │   │               │   │   ├───logEntries
│   │   │               │   │   └───weeklySummaries
│   │   │               │   └───entities
│   │   │               ├───repositories
│   │   │               ├───security
│   │   │               ├───services
│   │   │               │   └───impl
│   │   │               └───utils
```

## Steps to Contribute
Contribute if you want.
1. Open an issue if you're serious.
2. Fork it, clone it.
3. Make a branch:
   ```bash
    git checkout -b your-branch-name
4. Do your thing.
5. Commit and push.
6. Open a pull request.

## License
[MIT](https://choosealicense.com/licenses/mit/)
