package me.cyberproton.ocean.annotations;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
public @interface V1ApiRestController {
    @AliasFor(annotation = RestController.class)
    String value() default "";
}
