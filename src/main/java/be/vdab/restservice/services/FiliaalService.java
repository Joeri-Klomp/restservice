package be.vdab.restservice.services;

import be.vdab.restservice.domain.Filiaal;
import be.vdab.restservice.exceptions.FiliaalNietGevondenException;
import be.vdab.restservice.repositories.FiliaalRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FiliaalService {
    private final FiliaalRepository filiaalRepository;

    public FiliaalService(FiliaalRepository filiaalRepository) {
        this.filiaalRepository = filiaalRepository;
    }

    public Optional<Filiaal> findById(long id) {
        return filiaalRepository.findById(id);
    }

    public List<Filiaal> findAll() {
        return filiaalRepository.findAll();
    }

    @Transactional
    public void create(Filiaal filiaal) {
        filiaalRepository.save(filiaal);
    }

    @Transactional
    public void update(Filiaal filiaal) {
        filiaalRepository.save(filiaal);
    }

    @Transactional
    public void delete(long id) {
        try {
            filiaalRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new FiliaalNietGevondenException();
        }
    }
}
