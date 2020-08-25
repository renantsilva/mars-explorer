package br.com.elo7.marsexplorer.service;

import br.com.elo7.marsexplorer.model.Land;
import br.com.elo7.marsexplorer.repository.LandRepository;
import br.com.elo7.marsexplorer.validation.Errors;
import br.com.elo7.marsexplorer.validation.MarsExplorerError;
import br.com.elo7.marsexplorer.validation.MarsExplorerValidator;
import br.com.elo7.marsexplorer.validation.exceptions.BadRequestException;
import br.com.elo7.marsexplorer.validation.exceptions.NotFoundException;
import br.com.elo7.marsexplorer.validation.exceptions.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LandService {
    @Autowired
    private LandRepository repository;

    @Autowired
    private MarsExplorerValidator validator;

    @Autowired
    private ProbeService probeService;

    public Land create(Land land) {
        validate(land);
        return repository.save(land);
    }

    public Land findById(Long id) {
        if (id == null)
            throw new BadRequestException().withErrors(Errors.ID_REQUIRED);
        return repository.findById(id).orElseThrow(() -> new NotFoundException().withErrors(Errors.LAND_NOT_FOUND));
    }

    public Land update(Long id, Land land) {
        findById(id);
        land.setId(id);
        validate(land);
        return repository.save(land);
    }

    public void validate(Land land) {
        validator.throwableValidate(land);
        validateProbes(land);
    }

    public void validateProbes(Land land) {
        validateProbesPositions(land);
        land.getProbes().values().forEach(probe -> probe = probeService.findOrCreate(probe));
    }

    public void validateProbesPositions(Land land) {
        List<MarsExplorerError> positionErrors = land.getProbes().keySet().stream()
                .filter(position -> !position.isInside(land))
                .map(position -> Errors.POSITION_OUTSIDE_LAND(position))
                .collect(Collectors.toList());
        if (!positionErrors.isEmpty()) {
            throw new UnprocessableEntityException().withErrors(positionErrors);
        }
    }

    public void deleteById(Long id) {
        Land found = findById(id);
        repository.delete(found);
    }

}