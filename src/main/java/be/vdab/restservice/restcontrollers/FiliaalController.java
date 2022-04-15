package be.vdab.restservice.restcontrollers;

import be.vdab.restservice.domain.Filiaal;
import be.vdab.restservice.exceptions.FiliaalNietGevondenException;
import be.vdab.restservice.services.FiliaalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.TypedEntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
//Je typt @RestController bij een controller waarin de responses geen HTML bevatten, maar XML data of JSON data.
@RequestMapping("/filialen")
//Je moet de URL aangeven die een type entity hoort (/filialen hoort bij Filiaal entities). Je typt
//@ExposesResourceFor(Filiaal.class) voor de class:
@ExposesResourceFor(Filiaal.class)
@CrossOrigin(exposedHeaders = "Location")
//In hoofdstuk 16 van cursus Spring Advanced willen we dit project als backend applicatie gebruiker. Hiervoor hebben we
//CrossOrigin nodig. Standaard kan de frontend applicatie de backend applicatie niet aanspreken, met Cross Origin Resource
//Sharing kan dit wel.
//Als je @CrossOrigin typt voor de class FiliaalController, kan JavaScript code van andere websites alle requests doen
//die FiliaalController behandelt. Bij een CORS request bevat de response standaard slechts een beperkt aantal headers.
//De Location header ontbreekt. Je hebt in het voorbeeld straks deze header nodig. Met exposedHeaders = "Location"
//plaatst Spring deze header toch in de response.
public class FiliaalController {
    private final FiliaalService filiaalService;
    private final TypedEntityLinks.ExtendedTypedEntityLinks<Filiaal> links;

    public FiliaalController(FiliaalService filiaalService, EntityLinks links) {
        this.filiaalService = filiaalService;
        this.links = links.forType(Filiaal.class, Filiaal::getId);
        //Je maakt een ExtendedTypedEntityLinks met de forType method van EntityLinks. De 1° parameter is een entity.
        // De 2° parameter is een method reference naar de getter die de id van die entity geeft.
    }

    //Zonder HATEOAS:
//    @GetMapping("{id}")
//    Filiaal get(@PathVariable long id) {
//        return filiaalService.findById(id).orElseThrow(FiliaalNietGevondenException::new);
//    }

    //Met HATEOAS:
    @GetMapping("{id}")
    @Operation(summary = "Een filiaal zoeken op id")
    EntityModel<Filiaal> get(@PathVariable long id) {
        return filiaalService.findById(id).map(filiaal -> EntityModel.of(filiaal,
                        links.linkToItemResource(filiaal),
                        links.linkForItemResource(filiaal).slash("werknemers").withRel("werknemers")))
                .orElseThrow(FiliaalNietGevondenException::new);
    }

    @ExceptionHandler(FiliaalNietGevondenException.class)
    //@ExceptionHandler zorgt dat er een response gestuurd wordt als de exception optreedt
    @ResponseStatus(HttpStatus.NOT_FOUND)
        //we sturen dan de response NOT FOUND door
    void filiaalNietGevonden() {
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Een filiaal verwijderen")
    void delete(@PathVariable long id) {
        filiaalService.delete(id);
    }

    //Zonder HATEOAS
//    @PostMapping
//    void post(@RequestBody @Valid Filiaal filiaal) {
//        //Spring vertaalt met @RequestBody de data in de request body naar een Filiaal object en geeft dit door in de parameter filiaal.
//        //Als er validatiefouten zijn, wordt MethodArgumentNotValidException geworpen
//        filiaalService.create(filiaal);
//    }

    //Met HATEOAS
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Een filiaal toevoegen")
    HttpHeaders create(@RequestBody @Valid Filiaal filiaal) {
        filiaalService.create(filiaal);
        var headers = new HttpHeaders();
        //Je gebruikt HttpHeaders als return type. Je kan daarmee response headers opbouwen.
        headers.setLocation(links.linkToItemResource(filiaal).toUri());
        //Je vult de Location header van de response met de URI van het nieuwe filiaal.
        return headers;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    //Je definieert dat de method een response naar de browser stuurt als een MethodArgumentNotValidException optreedt.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
        //Bad Request is de statuscode van de response die moet worden gestuurd
    Map<String, String> verkeerdeData(MethodArgumentNotValidException e) {
        //De response is een Map. De key van elke entry wordt de naam van het foute attribuut. De value is de omschrijving
        //van de fout. Spring plaatst de fouten in de request data in de MethodArgumentNotValidException parameter.
        //getBindingResult().getFieldErrors() geeft een verzameling FieldError objecten: de Filiaal attributen met validatiefouten.
        //Je maakt van die verzameling een Map. De key is de naam van het foute attribuut.
        //De value is de omschrijving van de fout.
        return e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    @PutMapping("{id}")
    @Operation(summary = "Een filiaal wijzigen")
    void put(@PathVariable long id, @RequestBody @Valid Filiaal filiaal) {
        filiaalService.update(filiaal.withId(id));
    }

    //Maak een record dat enkel de id en de naam van een filiaal voorstelt:
    private record FiliaalIdNaam(long id, String naam) {
        FiliaalIdNaam(Filiaal filiaal) {
            this(filiaal.getId(), filiaal.getNaam());
            //Je roept vanuit je eigen constructor de constructor op die het record automatisch bevat.
            //Die constructor heeft als parameters id en naam.
        }
    }

    @GetMapping
    @Operation(summary = "Alle filialen zoeken")
    CollectionModel<EntityModel<FiliaalIdNaam>> findAll() {
        //CollectionModel stelt een response voor met een verzameling objecten en hyperlink(s). Elk object is hier een
        //EntityModel: data uit FiliaalIdNaam en een hyperlink.
        //We maken eerst een CollectionModel:
        return CollectionModel.of(
                //Je leest alle filialen in de database.
                filiaalService.findAll().stream()
                        .map(filiaal ->
                                //De data in dit EntityModel is een FiliaalIdNaam die je maakt op basis van het filiaal.
                                EntityModel.of(new FiliaalIdNaam(filiaal),
                                        //Je voegt de URL van het filiaal toe aan het EntityModel:
                                        links.linkToItemResource(filiaal)))
                        ::iterator, //Je maakt een Iterable<EntityModel> op basis van de stream van EntityModel objecten.
                // Je geeft die aan je CollectionModel.
                links.linkToCollectionResource());
                //Je voegt de URL van alle filialen toe aan je CollectionModel.
    }
}
