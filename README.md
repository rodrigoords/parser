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
java -jar parser-0.0.1.jar --accesslog=path/to/file --startDate=2017-01-01.00:00:00 --duration=daily --threshold=200
```

### arguments
* bulk_insert.batch_size: (optional, default: 20) The batch size used in the bulk insert flush clear.
* separatorRegex: (optional, default: \\|) Regex to parser file.
* accesslog: path to file, need to pass only the root path that contains the file example: accesslog=/tmp
* filename: (optional, default: access.log) The file name with extension

* startDate: start date to use as a filter.
* duration: can take only "hourly", "daily"
* threshold: delimiter parameter to use as a filter.
* datePattern: (optional, default: yyyy-MM-dd HH:mm:ss) date pattern to parse string as date.

### Useful Queries

```
select ip, count(*) total from wallethub_logs
where date between '2017-01-01.15:00:00' and '2017-01-01.15:59:59'
group by ip
having count(*) >= 200;
```

```
select * from wallethub_logs where ip = '192.168.234.82';
```

```
select * from blocked_ips;
```
