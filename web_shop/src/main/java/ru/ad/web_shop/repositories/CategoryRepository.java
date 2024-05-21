package ru.ad.web_shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ad.web_shop.entities.Category;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer> {
}
