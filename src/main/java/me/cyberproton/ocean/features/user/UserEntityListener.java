package me.cyberproton.ocean.features.user;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserEntityListener {
    private final UserElasticRepository userElasticRepository;

    @PostPersist
    @PostUpdate
    public void onPersist(UserEntity userEntity) {
        userElasticRepository.save(userEntity);
    }

    @PostRemove
    public void onRemove(UserEntity userEntity) {
        userElasticRepository.delete(userEntity);
    }
}
