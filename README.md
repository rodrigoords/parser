# parser
file parser and analyser

The project load and parse a .txt file with a configurable argument delimiter, put all the parsed data in a MySql database and analyse data with some business rules.

Project need a localhost MySql data base you can use docker compose to build and run the environment.
```
cd src/main/docker
docker-compose build
docker-compose run
````

With a correctly environment run with:

```
java -jar parser-0.0.1.jar --startDate=2017-01-01.00:00:00 --duration=daily --threshold=200
```

 ### argument
 * bulk_insert.batch_size: (optional, default: 20) The batch size used in the bulk insert flush clear.
 * separatorRegex: (optional, default: \\|) Regex to parser file.
 * accesslog: path to file.
 * filename: (optional, default: access.log) The file name with extension

 * startDate: start date to use as a filter.
 * duration: can take only "hourly", "daily"
 * threshold: delimiter parameter to use as a filter.
 * datePattern: (optional, default: yyyy-MM-dd HH:mm:ss) date pattern to parse string as date.
 