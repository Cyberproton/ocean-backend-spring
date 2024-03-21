package me.cyberproton.ocean.features.file;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiController;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@V1ApiController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

//    @GetMapping("/test")
//    public void test() {
//        fileService.test();
//    }
}
