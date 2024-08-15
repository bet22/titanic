package ru.bet.passengers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PassengerComponent {
    private final PassengerService passengerService;

    public void prepareModel(String search, String sortBy, Sort.Direction sortDir, String filter,
                             PageRequest pageRequest, Model model) {

        var passengers = passengerService.getPassengers(search, Sort.by(sortDir, sortBy), filter);
        var allFare = passengers.stream().map(Passenger::getFare)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var allSurvived = passengers.stream().filter(Passenger::getSurvived)
                .toList();

        var passengersWithRelativesCount = passengers.stream()
                .filter(it -> it.getParentsChildren() > 0 || it.getSiblingsSpouses() > 0)
                .map(it -> it.getParentsChildren() + it.getSiblingsSpouses())
                .reduce(0, Integer::sum);

        var passengerPage = new PageImpl<>(
                passengers.stream()
                        .skip((long) pageRequest.getPageNumber() * pageRequest.getPageSize())
                        .limit(pageRequest.getPageSize()).collect(Collectors.toList()),
                pageRequest,
                passengers.size());

        model.addAttribute("passengerPage", passengerPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir == Sort.Direction.ASC ? Sort.Direction.DESC : Sort.Direction.ASC);
        model.addAttribute("search", search);
        model.addAttribute("size", pageRequest.getPageSize());
        model.addAttribute("currentPage", pageRequest.getPageNumber());
        model.addAttribute("totalPages", passengerPage.getTotalPages());
        model.addAttribute("passengersWithRelativesCount", passengersWithRelativesCount);
        model.addAttribute("allFare", allFare);
        model.addAttribute("allSurvived", allSurvived.size());

    }

}