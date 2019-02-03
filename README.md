
Entities:

APIData:

    private int day;
    private int hour;
    private double temperature;
    private double pressure;

WeatherData: Data to be replied.

    private String dataId;
    private double dayAverage;
    private double nightAverage;
    private double pressureAvg;

///

One end point

GET /data/temperature/{city}/{country}/next/{days}/average

Validate inputs, return errors:

Response sample:

{
id: Berlin, DE
dayTimeAvg: 23.4,
nightTimeAvg: 10.2,
pressureAvg: 12321.2
}

OK: 200
Created: 201
Validation: 412
Created: 201
429 Too Many Requests
500 when 400 error is returned. {"cod":401, "message": "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."}

Days were added for future implementation. Not used.

///

The solution architecture will involve a cache with a ttl of 10 minutes, we can configure rhat nmber. In order
not to go every time to the api, we will have to get this number of ttl according to the number of requests
and the probability of change of tempreatures. 60 calls per minute are allowed, so we will be able to precalculate results every second, I beieve this is too much so I will set this TTL  one every 10 minutes because no major changes will afect the calculus.


Next 3 days will be considered from 6 to 6 for the next day.

I will use a cache because if this service hasa to be duplicated or instanciated N times in order to accept
as many requests as possible a centralized cache system will be used to have all data up to date and have a
great response time, and also to prevent more than 60 calls per minute to the weather API.


According to the max result we have 50 bytes to store:

{
dayTimeAvg: 23.4,
nightTimeAvg: 10.2
pressureAvg: 12321.2
}

We have according to iso 31166: "103,034 locations in 249 countries"

https://www.unece.org/cefact/locode/welcome.html

So 3 results per 103,034 cities of 50 bytes: 15455100  bytes = 14.73 Mb

Tha will be the cache maximum storage.


Data response analysis:

http://api.openweathermap.org/data/2.5/forecast?q=M%C3%BCnchen,DE&appid=663928080a513ba860832815587e6a10&units=metric

I will use metric for celcius, time will be UTC and pressure hPA.


Data that will be manage:

Data

day 03
hour 18
temp 2
pressure 4000

One for daytime, nighttime and presurre filtering with java8 streams:

1/ get data
2/ filter by next 3 days (day > today AND day < day plus 3 days)
3/ get avg temp
4/ get avg pressure


Date coneversions:

LocalDateTime dateTime = LocalDateTime.parse("2018-05-05T11:50:55");

"dt_txt":"2019-02-02 21:00:00"

a T will be added.

Will be managed in Java8 LocalTime.

LocalDateTime.now();
localDateTime.plusDays(3);
LocalTime now = LocalTime.now();
now.getHour();
LocalTime now = LocalTime.now();
System.out.println(now.getHour());


Instructions to RUN the service:

docker install redis
docker run -p 6379:6379  -h localhost redis
run springboot app

Manual TESTS:

localhost:8080/data/temperature/Cologne/DE/next/3/average
localhost:8080/data/temperature/Berlin/DE/next/3/average
localhost:8080/data/temperature/München/DE/next/3/average


Response Sample:

{
    "dataId": "Cologne,DE",
    "dayAverage": 0.5780000000000001,
    "nightAverage": 0,
    "pressureAvg": 1012.644
}

The solution is returning all data required in one 
response, this can be easily decoupled as needed.

Pending:

1) I found an encoding issue with Munchen,DE that has
to be fixed.

2) Also webflux will be considered for better performance.

3) url must be placed in properties file.

4) I have worked with Country as well if needed in the future.

5) HTTP code status can be managed with a business exception
handler.

6) Metric system must be a property.

7) Integration tests to be added with docker.

8) Redis TTL test to be implemented.