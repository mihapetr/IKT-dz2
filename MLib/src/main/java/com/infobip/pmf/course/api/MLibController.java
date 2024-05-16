package com.infobip.pmf.course.api;

import com.infobip.pmf.course.Account;
import com.infobip.pmf.course.Warehouse;
import com.infobip.pmf.course.sLibrary;
import com.infobip.pmf.course.storage.UserEntity;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/libraries")
@RestController
public class MLibController {

    private final Warehouse wh;
    public MLibController(Warehouse warehouse) {
        wh = warehouse;
    }

    @GetMapping
    List<sLibrary> allItems(
            @RequestParam(name = "groupId", required = false) String groupId,
            @RequestParam(name = "artifactId", required = false) String artifactId,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) int size
    ) {
        return wh.allLibItems(groupId, artifactId, page, size);
    }

    /*@GetMapping("/users")
    List<Account> allUsers() {
        return wh.allUsers();
    }*/

}
