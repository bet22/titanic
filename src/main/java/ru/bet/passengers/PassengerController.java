package ru.bet.passengers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerComponent passengerComponent;

    @GetMapping("/passengers")
    public String getPassengers(@RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "50") Integer size,
                                @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                                @RequestParam(name = "sortDir", defaultValue = "ASC") Sort.Direction sortDir,
                                @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                @RequestParam(required = false) String filter,
                                Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        passengerComponent.prepareModel(search, sortBy, sortDir, filter, pageRequest, model);

        return "passengers";
    }
}