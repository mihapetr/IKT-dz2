package com.infobip.pmf.course.api;

import com.infobip.pmf.course.Warehouse;
import com.infobip.pmf.course.sLibrary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/libraries")
@RestController
public class MLibController {

    private final Warehouse wh;
    public MLibController(Warehouse warehouse) {
        wh = warehouse;
    }

    @GetMapping
    String allItems() {
        return wh.test("Ovo dohvaća sve biblioteke.");
    }
}
