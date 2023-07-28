package dev.jonclarke.samplerestservice.dataaccess;

import dev.jonclarke.samplerestservice.models.MovieDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<MovieDataModel, Integer> {
}
