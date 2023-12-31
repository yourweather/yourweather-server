package com.umc.yourweather.repository;

import com.umc.yourweather.domain.entity.Advertisement;
import java.util.Optional;

public interface AdvertisementRepository {
    Advertisement save(Advertisement advertisement);
    Optional<Advertisement> findById(Long id);
    Optional<Advertisement> findByAdId(String adId);
    void delete(Advertisement advertisement);
}
