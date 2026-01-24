# make-every-mile-count
Let's see if you're earning enough compared to what you're losing every mile you drive for deliveries.

# Main Idea

As I've been researching for this project, it's a similar idea to GigU, and sites/apps like that, that have been born
out of the rideshare boom. I want to let drivers know if they're making a profit on delivery runs and how much. 
I plan on starting small with calculating mileage costs based on city/metro/zip average cost of gas versus how much you made. I'm simply trying to help drivers be more informed as
they deliver. The decision to take certain delivery trips can be more complicated than just profit over costs, and varies based on the car you drive.

## Tech Stack

- Java

## APIs/Dependencies
- [CarAPI](https://carapi.app/api#/)
- [Jackson](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind)
- [Apache POI](https://poi.apache.org/)
- [EIA API](https://www.eia.gov/opendata/)

## Future work

- Add in other costs (Insurance, percentage used similar to stride's tax calculations, add repair costs)
- Expand to application/extension use (Applications like GigU are only on Android for now, so they're might be an opportunity there)
- Make it an assistant
- Mileage tracker

## Notes

- For now I'm not going to be able to keep track of mileage. Stride is a great app that will keep track
of your mileage. 
- This is not tax/financial advice. Please consult a tax professional for your specific situation.
- This is a personal project and not affiliated with any rideshare/delivery companies.
- If you have suggestions or want to contribute, please reach out!
- Run locally on command line with 'mvn clean compile
mvn exec:java -Dexec.mainClass=ui.Main'
