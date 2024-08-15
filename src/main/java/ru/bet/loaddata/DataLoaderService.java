package ru.bet.loaddata;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bet.passengers.Passenger;
import ru.bet.passengers.PassengerClass;
import ru.bet.passengers.PassengerRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class DataLoaderService {

    @Value("${app.data-url}")
    private String dataUrl;

    private final PassengerRepository passengerRepository;

    @PostConstruct
    public void loadData() throws IOException {
        if (passengerRepository.count() == 0) {
            downloadAndSaveData();
        }
    }

    private void downloadAndSaveData() throws IOException {
        URL url = new URL(dataUrl);
        try (InputStreamReader reader = new InputStreamReader(url.openStream())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());

            passengerRepository.saveAll(
                    csvParser.getRecords()
                            .stream()
                            .map(this::preparePassenger)
                            .toList()
            );
        }
    }

    //todo можно подключить маппер
    private Passenger preparePassenger(CSVRecord record) {
        Passenger passenger = new Passenger();
        passenger.setPclass(parsePassengerClass(record.get("Pclass")));
        passenger.setSurvived(parseSurvived(record.get("Survived")));
        passenger.setName(record.get("Name"));
        passenger.setFare(new BigDecimal(record.get("Fare")));
        passenger.setAge(Double.parseDouble(record.get("Age")));
        passenger.setSex(record.get("Sex"));
        passenger.setParentsChildren(Integer.parseInt(record.get("Parents/Children Aboard")));
        passenger.setSiblingsSpouses(Integer.parseInt(record.get("Siblings/Spouses Aboard")));
        return passenger;
    }

    private PassengerClass parsePassengerClass(String pclass) {
        return switch (pclass) {
            case "1" -> PassengerClass.FIRST;
            case "2" -> PassengerClass.SECOND;
            case "3" -> PassengerClass.THIRD;
            default -> throw new IllegalArgumentException("Неизвестный класс пассажира: " + pclass);
        };
    }

    private Boolean parseSurvived(String survived) {
        return "1".equals(survived);
    }
}
