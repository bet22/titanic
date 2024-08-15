package ru.bet.passengers;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;

    @Cacheable("passengers")
    @Transactional(readOnly = true)
    public List<Passenger> getPassengers(String search, Sort sort, String filter) {
        return passengerRepository.findByNameContainingIgnoreCase(search, sort)
                .stream()
                .filter(getPredicate(filter))
                .toList();

    }

    private Predicate<Passenger> getPredicate(String filter) {
        return passenger -> filter == null
                || ("adults".equals(filter) && passenger.getAge() > 16)
                || ("survived".equals(filter) && passenger.getSurvived()
                || ("male".equals(filter) && passenger.getSex().equals("male"))
                || ("noRelatives".equals(filter) && passenger.getParentsChildren() == 0 && passenger.getSiblingsSpouses() == 0)
        );
    }
}